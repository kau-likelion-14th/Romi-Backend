package likelion14th.lte.Entity.statics.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import likelion14th.lte.Entity.statics.dto.StatisticResponse;
import likelion14th.lte.Entity.statics.entity.Statistic;
import likelion14th.lte.Entity.statics.entity.StatWeek;
import likelion14th.lte.Entity.todo.repository.TodoDateRepository;
import likelion14th.lte.global.api.ErrorCode;
import likelion14th.lte.global.exception.GeneralException;
import likelion14th.lte.user.entity.User;
import likelion14th.lte.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticService {

    private static final int BATCH_PAGE_SIZE = 500;

    private final UserRepository userRepository;
    private final TodoDateRepository todoDateRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public StatisticResponse getStatistic(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        Statistic statistic = user.getStatistic();

        return StatisticResponse.from(statistic);
    }


    @Transactional
    public void updateStatistic(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorCode.USER_NOT_FOUND));

        applyDailyUpdate(user);
    }

    @Transactional
    public void updateAllStatistics() {
        int page = 0;
        Page<User> userPage;

        do {
            userPage = userRepository.findAll(PageRequest.of(page, BATCH_PAGE_SIZE));

            userPage.getContent().forEach(this::applyDailyUpdate);

            entityManager.flush();
            entityManager.clear();

            page++;
        } while (userPage.hasNext());
    }


    private void applyDailyUpdate(User user) {
        Statistic statistic = user.getStatistic();
        Long userId = user.getId();
        LocalDate yesterday = LocalDate.now().minusDays(1);


        boolean hasCompleted = todoDateRepository.existsByTodo_User_IdAndDateAndCompleted(userId, yesterday, true);
        boolean hasFailed = todoDateRepository.existsByTodo_User_IdAndDateAndCompleted(userId, yesterday, false);
        boolean isSuccessDay = hasCompleted && !hasFailed;


        statistic.increaseStreakIfSuccess(isSuccessDay);


        if (isSuccessDay) {
            statistic.getStatWeeks().stream()
                    .filter(w -> w.getWeek().toDayOfWeek() == yesterday.getDayOfWeek())
                    .findFirst()
                    .ifPresent(StatWeek::increaseCount);
        }


        LocalDate start = yesterday.minusDays(30);
        long completedCount = todoDateRepository.countByTodo_User_IdAndDateBetweenAndCompleted(userId, start, yesterday, true);
        long failedCount = todoDateRepository.countByTodo_User_IdAndDateBetweenAndCompleted(userId, start, yesterday, false);
        long total = completedCount + failedCount;

        int monthPercent = (total == 0) ? 0 : (int) ((completedCount * 100) / total);
        statistic.updateMonthPercent(monthPercent);
    }

}