package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private PointService pointService;

    @Test
    @DisplayName("충전 금액이 0원일 경우 에러 테스트")
    public void chargeAmountZeroTest() {
        // given
        long id = 1L;
        long amount = 0L;

        // then
        assertThatThrownBy(() -> {
            pointService.charge(id, amount);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("충전 금액을 잘못 입력하셨습니다.");

    }

    @Test
    @DisplayName("충전 테스트")
    public void chargeTest() {
        // given
        long id = 1L;
        long amount = 1000L;
        UserPoint userPoint = new UserPoint(id, amount, System.currentTimeMillis());
        given(userPointTable.insertOrUpdate(id, amount)).willReturn(userPoint);

        // when
        UserPoint resultPoint = pointService.charge(id, amount);

        // then
        assertEquals(amount, resultPoint.point());
    }
}
