/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

public class DateUtilsTest {

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    @Test
    public void canClearTime() throws Exception {
	Date withTime = sdf.parse("21-12-2022 10:20:56");
        Date withTimeCleared = DateUtils.clearTime(withTime);
        assertEquals(sdf.parse("21-12-2022 00:00:00"), withTimeCleared);
    }

    @Test
    public void canClearTime2() throws Exception {
	Date withTime = sdf.parse("21-12-2022 00:00:00");
        Date withTimeCleared = DateUtils.clearTime(withTime);
        assertEquals(sdf.parse("21-12-2022 00:00:00"), withTimeCleared);
    }

    @Test
    public void canClearTime3() throws Exception {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 25);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        c.set(Calendar.YEAR, 2009);
        Date withTime = c.getTime();
        Date withTimeCleared = DateUtils.clearTime(withTime);
        assertEquals(sdf.parse("25-12-2009 00:00:00"), withTimeCleared);
    }
}
