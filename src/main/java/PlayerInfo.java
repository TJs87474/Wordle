public class PlayerInfo {
    private int wins;
    private int gamesPlayed;

    PlayerInfo() {
        this.wins = 0;
        this.gamesPlayed = 0;
    }

    public int getTotalPlays(){
        return this.gamesPlayed;
    }

    public void addGamesPlayed(int gamesPlayed) {
        this.gamesPlayed += gamesPlayed;
    }

    public void addWins(int wins) {
        this.wins += wins;
    }

    public double winRate() {
        if (gamesPlayed != 0) {
        return (double) this.wins / this.gamesPlayed;
        }
        return 0.0;
    }

    public String toString() {
        return String.format("Wins: %d, Games Played: %d", this.wins, this.gamesPlayed);

    }
}