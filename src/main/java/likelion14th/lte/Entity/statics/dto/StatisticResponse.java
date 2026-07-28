package likelion14th.lte.Entity.statics.dto;

import likelion14th.lte.Entity.statics.entity.Statistic;
import likelion14th.lte.Entity.statics.entity.WeekEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticResponse {

    private int streak;
    private int monthPercent;
    private WeekEnum mostTodoWeek;

    private StatisticResponse(int streak, int monthPercent, WeekEnum mostTodoWeek) {
        this.streak = streak;
        this.monthPercent = monthPercent;
        this.mostTodoWeek = mostTodoWeek;
    }


    public static StatisticResponse from(Statistic statistic) {
        return new StatisticResponse(
                statistic.getStreak(),
                statistic.getMonthPercent(),
                statistic.mostTodoWeek()
        );
    }
}