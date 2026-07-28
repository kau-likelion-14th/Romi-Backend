package likelion14th.lte.Entity.statics.entity;

import jakarta.persistence.*;
import likelion14th.lte.Entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Table(name = "statistic")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // new Statistic() 외부 호출 차단
public class Statistic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int streak; // 연속 성공일

    @Column(nullable = false)
    private int monthPercent; // 최근 30일 완료율

    @OneToMany(mappedBy = "statistic", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StatWeek> statWeeks = new ArrayList<>();

    private Statistic(int streak, int monthPercent) {
        this.streak = streak;
        this.monthPercent = monthPercent;
    }


    public static Statistic create() {
        Statistic statistic = new Statistic(0, 0);
        statistic.initializeWeeks();
        return statistic;
    }


    private void initializeWeeks() {
        for (WeekEnum week : WeekEnum.values()) {
            StatWeek statWeek = StatWeek.createWithZeroCount(week, this);
            this.statWeeks.add(statWeek);
        }
    }


    public WeekEnum mostTodoWeek() {
        return statWeeks.stream()
                .max(Comparator.comparingInt(StatWeek::getCount))
                .map(StatWeek::getWeek)
                .orElse(null);
    }


    public void increaseStreakIfSuccess(boolean isSuccess) {
        this.streak = isSuccess ? this.streak + 1 : 0;
    }


    public void updateMonthPercent(int monthPercent) {
        this.monthPercent = monthPercent;
    }
}