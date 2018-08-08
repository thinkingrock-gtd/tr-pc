package au.com.trgtd.tr.cal.api;

//import au.com.trgtd.tr.cal.test.TestFrame;
import javax.swing.SwingUtilities;
import org.junit.*;

public class DayViewTest {
    
    public DayViewTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of setDayViewCtlr method, of class DayView.
     */
    @Test
    public void testDayView() {
        System.out.println("test day view");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
//                TestFrame.main(null);
            }
            
        });
    }

}
