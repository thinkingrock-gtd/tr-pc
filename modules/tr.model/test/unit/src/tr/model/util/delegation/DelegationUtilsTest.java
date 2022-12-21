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
package tr.model.util.delegation;

import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import static tr.model.util.delegation.DelegationData.Type.DELEGATION;
import static tr.model.util.delegation.DelegationData.Type.RESPONSE;

/**
 * Test DelegationUtils.
 *
 * @author Jeremy Moore
 */
public class DelegationUtilsTest {

    private static final DelegationData TEST_DD_ALL_VALUES
            = new DelegationDataBuilder(DELEGATION)
            .id(123)
            .reply("reply@gmail.com")
            .success("Maka a {}]}}}million {{{%^%^dfdollars")
            .time("10 \n\rmin")
            .priority("Should")
            .topic("Phone calls")
            .start(new Date(2016, 11, 21))
            .due(new Date(2016, 12, 23))
            .done(null)
            .make();

    private static final DelegationData TEST_DD_NULLS
            = new DelegationDataBuilder(RESPONSE)
            .id(42)
            .reply("manager@gmail.com")
            .done(new Date(2016, 9, 4))
            .make();

    private static final DelegationData[] TEST_DD_ITEMS = new DelegationData[]{
        TEST_DD_ALL_VALUES,
        TEST_DD_NULLS
    };

    /**
     * Test of serialize and de-serialize methods, of class DelegateDataUtils.
     */
    @Test
    public void testSerializeDeserialize() {
        System.out.println("serialize - deserialize");

        for (DelegationData dd : TEST_DD_ITEMS) {
            
            assertTrue(DelegationUtils.deepEquals(dd, dd));
            
            System.out.println(DelegationUtils.serialize(dd));
            
            DelegationData dd2 = DelegationUtils.deserialize(DelegationUtils.serialize(dd));
            
            System.out.println(DelegationUtils.serialize(dd2));
            
            
            assertTrue(DelegationUtils.deepEquals(dd, DelegationUtils.deserialize(DelegationUtils.serialize(dd))));
        }
    }

    /**
     * Test of extract method, of class DelegateDataUtils.
     */
    @Test
    public void testExtract() {
        System.out.println("extract");

        for (DelegationData dd : TEST_DD_ITEMS) {
            String ss = DelegationUtils.serialize(dd);
            String ls = "d{}{}{}afafa\n\n\n\n\n\n\r\t " + ss + "da{}{}{}fa";
            String ds = DelegationUtils.extractSerializedString(ls);
            
            System.out.println(ss);
            System.out.println(ds);
            
            assertEquals(ss, ds);
        } 
    }
    
    
    @Test
    public void canSerializeAndDeserializeTypeDelegation() {
        String ss = "{\"reply\":\"trstufftest@gmail.com\",\"type\":0,\"time\":\"10 min\",\"priority\":\"MIT\",\"topic\":\"None\",\"id\":139398}";
        DelegationData dd = DelegationUtils.deserialize(ss);
        String out = DelegationUtils.serialize(dd);
        assertEquals(ss, out);
    }

    @Test
    public void canSerializeAndDeserializeTypeResponse() {
        String ss = "{\"reply\":\"trstufftest@gmail.com\",\"type\":1,\"done\":599999,\"id\":139398}";
        DelegationData dd = DelegationUtils.deserialize(ss);
        String out = DelegationUtils.serialize(dd);
        assertEquals(ss, out);
    }

    @Test
    public void cannotDeserializeWithMissingType() {
        String ss = "{\"reply\":\"trstufftest@gmail.com\",\"done\":599999,\"id\":139398}";
        DelegationData dd = DelegationUtils.deserialize(ss);
        assertNull(dd);
    }
}
