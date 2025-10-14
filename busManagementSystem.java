import java.util.*;

class Passenger {
    String name;
    int seatNo;
    Passenger next;

    Passenger(String name, int seatNo) {
        this.name = name;
        this.seatNo = seatNo;
        this.next = null;
    }
}

class BusTicketSystem {
    private Passenger head = null;
    private Queue<String> waitingList;
    private Stack<Passenger> cancelHistory;
    private final int MAX_SEATS = 5;
    private int bookedSeats = 0;

    // New tracking lists
    private List<Long> bookingTimes;
    private List<Long> cancelTimes;

    BusTicketSystem() {
        waitingList = new LinkedList<>();
        cancelHistory = new Stack<>();
        bookingTimes = new ArrayList<>();
        cancelTimes = new ArrayList<>();
    }

    void bookTicket(String name) {
        if (bookedSeats < MAX_SEATS) {
            bookedSeats++;
            Passenger newPassenger = new Passenger(name, bookedSeats);
            addPassenger(newPassenger);
            bookingTimes.add(System.currentTimeMillis()); // track booking time
            System.out.println("✅ Ticket booked for " + name + " | Seat No: " + bookedSeats);
        } else {
            waitingList.add(name);
            System.out.println("⏳ No seats available! " + name + " added to waiting list.");
        }
    }

    private void addPassenger(Passenger newP) {
        if (head == null) head = newP;
        else {
            Passenger temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = newP;
        }
    }

    void cancelTicket(int seatNo) {
        if (head == null) {
            System.out.println("❌ No bookings found.");
            return;
        }
        Passenger temp = head, prev = null;
        while (temp != null && temp.seatNo != seatNo) {
            prev = temp;
            temp = temp.next;
        }
        if (temp == null) {
            System.out.println("❌ Seat not found.");
            return;
        }

        if (prev == null) head = temp.next;
        else prev.next = temp.next;

        bookedSeats--;
        cancelHistory.push(temp);
        cancelTimes.add(System.currentTimeMillis()); // track cancellation time
        System.out.println("🚫 Ticket canceled for " + temp.name + " | Seat No: " + temp.seatNo);

        if (!waitingList.isEmpty()) {
            String nextPassenger = waitingList.poll();
            bookTicket(nextPassenger);
            System.out.println("🎟️ Seat reassigned to " + nextPassenger + " from waiting list.");
        }
    }

    void undoCancel() {
        if (cancelHistory.isEmpty()) {
            System.out.println("❌ Nothing to undo.");
            return;
        }
        Passenger lastCanceled = cancelHistory.pop();
        bookTicket(lastCanceled.name);
        System.out.println("↩️ Undo successful: Rebooked for " + lastCanceled.name);
    }

    void displayBookings() {
        System.out.println("\n📋 Current Bookings:");
        Passenger temp = head;
        if (temp == null) {
            System.out.println("No confirmed bookings.");
            return;
        }
        while (temp != null) {
            System.out.println("Seat " + temp.seatNo + ": " + temp.name);
            temp = temp.next;
        }
    }

    void displayWaitingList() {
        System.out.println("\n🕒 Waiting List:");
        if (waitingList.isEmpty()) System.out.println("No one in waiting list.");
        else for (String name : waitingList) System.out.println(name);
    }

    // --------------------------
    // 🧮 NEW AUTOMATION SECTION
    // --------------------------

    void predictBusStatus() {
        if (bookingTimes.size() < 2) {
            System.out.println("\n📊 Not enough data for prediction yet.");
            return;
        }

        // Calculate average booking interval (ms)
        long totalInterval = 0;
        for (int i = 1; i < bookingTimes.size(); i++) {
            totalInterval += (bookingTimes.get(i) - bookingTimes.get(i - 1));
        }
        long avgBookingInterval = totalInterval / (bookingTimes.size() - 1);

        int remainingSeats = MAX_SEATS - bookedSeats;
        double minutesToFull = (avgBookingInterval * remainingSeats) / 60000.0;

        // Estimate cancellation probability
        double cancelProb = 0.0;
        if (!cancelTimes.isEmpty()) {
            cancelProb = Math.min(1.0, cancelTimes.size() / (double) bookingTimes.size());
        }

        System.out.println("\n📈 PREDICTION REPORT:");
        System.out.printf("Seats booked: %d / %d%n", bookedSeats, MAX_SEATS);
        System.out.printf("Estimated time until bus is full: %.2f minutes%n", minutesToFull);
        System.out.printf("Estimated cancellation probability: %.1f%%%n", cancelProb * 100);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BusTicketSystem bus = new BusTicketSystem();

        while (true) {
            System.out.println("\n--- BUS TICKET MANAGEMENT ---");
            System.out.println("1. Book Ticket");
            System.out.println("2. Cancel Ticket");
            System.out.println("3. Undo Last Cancel");
            System.out.println("4. Show Bookings");
            System.out.println("5. Show Waiting List");
            System.out.println("6. Predict Bus Status");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1:
                    System.out.print("Enter passenger name: ");
                    bus.bookTicket(sc.nextLine());
                    break;
                case 2:
                    System.out.print("Enter seat number to cancel: ");
                    bus.cancelTicket(sc.nextInt());
                    break;
                case 3:
                    bus.undoCancel();
                    break;
                case 4:
                    bus.displayBookings();
                    break;
                case 5:
                    bus.displayWaitingList();
                    break;
                case 6:
                    bus.predictBusStatus();
                    break;
                case 7:
                    System.out.println("🚍 Exiting...");
                    return;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }
}
