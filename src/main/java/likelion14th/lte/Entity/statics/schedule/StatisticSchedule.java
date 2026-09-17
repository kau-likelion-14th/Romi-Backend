package likelion14th.lte.Entity.statics.schedule;

import likelion14th.lte.Entity.statics.service.StatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StatisticSchedule {

    private final StatisticService statisticService;

    @Scheduled(cron = "0 10 0 * * *")
    public void runUpdateAllStatistics() {
        log.info("[StatisticSchedule] 전체 유저 통계 갱신 배치 시작");
        statisticService.updateAllStatistics();
        log.info("[StatisticSchedule] 전체 유저 통계 갱신 배치 종료");
    }

}