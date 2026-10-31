
package fi.jyu.ties4520.cottage;

public class BookingRequestTestRun {

    public static void main(String[] args) {

        BookingRequest request = new BookingRequest(
                "Anna",
                4,
                2,
                200,
                "City1",
                30,
                5,
                "15.11.2026",
                2
        );

        System.out.println(request);
    }
}
