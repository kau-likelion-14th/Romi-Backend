package likelion14th.lte.Entity.statics.entity;

import jakarta.persistence.*;
import likelion14th.lte.Entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "stat_week")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatWeek extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private WeekEnum week;

    @Column(nullable = false)
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statistic_id", nullable = false)
    private Statistic statistic;

    private StatWeek(WeekEnum week, Statistic statistic) {
        this.week = week;
        this.count = 0;
        this.statistic = statistic;
    }


    static StatWeek createWithZeroCount(WeekEnum week, Statistic statistic) {
        return new StatWeek(week, statistic);
    }

    public void increaseCount() {
        this.count += 1;
    }

}