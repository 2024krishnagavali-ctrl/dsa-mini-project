import java.util.LinkedList;
import java.util.Scanner;

// Song class
class Song {
    String name;
    String artist;
    String album;
    int duration; // in seconds

    public Song(String name, String artist, String album, int duration) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return name + " by " + artist + " (" + album + ") [" + duration + "s]";
    }
}

// Song Library using LinkedList
class SongLibrary {
    LinkedList<Song> songs;

    public SongLibrary() {
        songs = new LinkedList<>();
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void displayAllSongs() {
        if (songs.isEmpty()) {
            System.out.println("No songs in the library.");
            return;
        }
        System.out.println("Song Library:");
        int i = 1;
        for (Song s : songs) {
            System.out.println(i + ". " + s);
            i++;
        }
    }

    public Song getSong(int index) {
        if (index >= 0 && index < songs.size()) {
            return songs.get(index);
        } else {
            return null;
        }
    }

    public int size() {
        return songs.size();
    }
}

// Playlist Manager with Queue and Stack
class PlaylistManager {
    LinkedList<Song> recentlyPlayed; // Queue
    LinkedList<Song> undoStack;       // Stack
    int maxRecent;

    public PlaylistManager(int maxRecent) {
        recentlyPlayed = new LinkedList<>();
        undoStack = new LinkedList<>();
        this.maxRecent = maxRecent;
    }

    public void playSong(Song song) {
        System.out.println("Playing: " + song);
        // Add to recently played queue
        recentlyPlayed.addLast(song);
        if (recentlyPlayed.size() > maxRecent) {
            recentlyPlayed.removeFirst(); // maintain max size
        }
        // Push to undo stack
        undoStack.push(song);
    }

    public void skipSong(Song song) {
        System.out.println("Skipped: " + song);
        // Push skipped song to undo stack
        undoStack.push(song);
    }

    public void undoLastAction() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        Song last = undoStack.pop();
        System.out.println("Undoing last action. You can replay: " + last);
        playSong(last);
    }

    public void showRecentlyPlayed() {
        if (recentlyPlayed.isEmpty()) {
            System.out.println("No recently played songs.");
            return;
        }
        System.out.println("Recently Played Songs:");
        for (Song s : recentlyPlayed) {
            System.out.println("- " + s);
        }
    }

    // Simple prediction: pick song not in recently played
    public void predictNextSong(SongLibrary library) {
        for (Song s : library.songs) {
            if (!recentlyPlayed.contains(s)) {
                System.out.println("Predicted Next Song: " + s);
                return;
            }
        }
        System.out.println("No prediction available (all songs recently played).");
    }
}

// Main Program
public class busManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        SongLibrary library = new SongLibrary();
        PlaylistManager manager = new PlaylistManager(5); // Keep last 5 songs

        // Sample songs
        library.addSong(new Song("Shape of You", "Ed Sheeran", "Divide", 240));
        library.addSong(new Song("Blinding Lights", "The Weeknd", "After Hours", 200));
        library.addSong(new Song("Levitating", "Dua Lipa", "Future Nostalgia", 203));
        library.addSong(new Song("Believer", "Imagine Dragons", "Evolve", 210));
        library.addSong(new Song("Perfect", "Ed Sheeran", "Divide", 265));

        int choice;
        do {
            System.out.println("\n--- Predictive Song Playlist ---");
            System.out.println("1. Display All Songs");
            System.out.println("2. Play a Song");
            System.out.println("3. Skip a Song");
            System.out.println("4. Undo Last Action");
            System.out.println("5. Show Recently Played");
            System.out.println("6. Predict Next Song");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    library.displayAllSongs();
                    break;
                case 2:
                    library.displayAllSongs();
                    System.out.print("Enter song number to play: ");
                    int playIndex = sc.nextInt() - 1;
                    sc.nextLine();
                    Song playSong = library.getSong(playIndex);
                    if (playSong != null) {
                        manager.playSong(playSong);
                    } else {
                        System.out.println("Invalid song selection.");
                    }
                    break;
                case 3:
                    library.displayAllSongs();
                    System.out.print("Enter song number to skip: ");
                    int skipIndex = sc.nextInt() - 1;
                    sc.nextLine();
                    Song skipSong = library.getSong(skipIndex);
                    if (skipSong != null) {
                        manager.skipSong(skipSong);
                    } else {
                        System.out.println("Invalid song selection.");
                    }
                    break;
                case 4:
                    manager.undoLastAction();
                    break;
                case 5:
                    manager.showRecentlyPlayed();
                    break;
                case 6:
                    manager.predictNextSong(library);
                    break;
                case 7:
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 7);

        sc.close();
    }
}