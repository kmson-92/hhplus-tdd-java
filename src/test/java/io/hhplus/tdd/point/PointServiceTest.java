package io.hhplus.tdd.point;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PointServiceTest {

    @Autowired
    PointService pointService;

    @Test
    public void testChargePoint() {
        UserPoint result = pointService.charge(1L, 1000L);

        assertEquals(1L, result.id());
        assertEquals(1000L, result.point());
    }
}
