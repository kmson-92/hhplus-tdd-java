package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class PointService {

    private final UserPointTable userPointTable;

    @Autowired
    public PointService(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    public UserPoint charge(long id, long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액을 잘못 입력하셨습니다.");
        }

        return userPointTable.insertOrUpdate(id, amount);
    }

    public UserPoint point(long id) {
        return userPointTable.selectById(id);
    }

}
