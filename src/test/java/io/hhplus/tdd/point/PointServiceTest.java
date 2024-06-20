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
import static org.mockito.Mockito.when;

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
        given(userPointTable.insertOrUpdate(id, amount)).willReturn(createUserPoint(id, amount));

        // when
        UserPoint resultPoint = pointService.charge(id, amount);

        // then
        assertEquals(amount, resultPoint.point());
    }

    @Test
    @DisplayName("포인트 조회")
    public void pointTest() {
        // given
        long id = 1L;
        long amount = 1000L;
        given(userPointTable.selectById(id)).willReturn(createUserPoint(id, amount));

        // when
        UserPoint resultPoint = pointService.point(id);

        // then
        assertEquals(amount, resultPoint.point());
    }

    private UserPoint createUserPoint(long id, long amount) {
        return new UserPoint(id, amount, System.currentTimeMillis());
    }

    @Test
    @DisplayName("잔고가 부족할 경우, 포인트 사용 실패")
    public void useFailTest() {
        // given
        long id = 1L;
        long amount = 0L;
        given(userPointTable.selectById(id)).willReturn(createUserPoint(id, amount));

        // then
        assertThatThrownBy(() -> {
            pointService.use(id, amount);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잔액이 부족합니다.");
    }

    @Test
    @DisplayName("잔고가 충분할 경우, 포인트 사용")
    public void useTest() {
        // given
        long id = 1L;
        long amount = 1000L;
        long useAmount = 500L;
        long resultAmount = amount - useAmount;
        given(userPointTable.selectById(id)).willReturn(createUserPoint(id, amount));

        // when
        when(userPointTable.insertOrUpdate(id, resultAmount)).thenReturn(createUserPoint(id, resultAmount));
        UserPoint result = pointService.use(id, useAmount);

        // then
        assertEquals(resultAmount, result.point());
    }


}
