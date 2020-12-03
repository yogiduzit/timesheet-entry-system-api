package com.corejsf.model.timesheet;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class TimesheetRowTest {

    @Test
    public void createEmptyTimesheetRow() {
        final TimesheetRow row = new TimesheetRow();
        validate(row, null, 0, null, new BigDecimal[Timesheet.DAYS_IN_WEEK], null);
    }

    @Test
    public void createTimesheetRowWithParams() {
        final int projectId = 123;
        final String workPackage = "112411";
//        BigDecimal[] hours = new BigDecimal[]
        final TimesheetRow row = new TimesheetRow();
        validate(row, null, 0, null, new BigDecimal[Timesheet.DAYS_IN_WEEK], null);
    }

    private void validate(TimesheetRow row, String id, int projectId, String workPackage, BigDecimal[] hours,
            String comments) {
        assertEquals(id, row.getId());
        assertEquals(projectId, row.getProjectID());
        assertEquals(workPackage, row.getWorkPackage());
        assertEquals(comments, row.getNotes());
        assertArrayEquals(hours, row.getHoursForWeek());
    }

}
