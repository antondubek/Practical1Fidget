import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class LeaderBoard {
    private PApplet p;
    private Game game;

    // Leaderboard Initialisation
    public char[] letters = new char[4]; // 4 letters
    public int index=0;                  // which letter is active
    public String result="";
    Table table;
    String savegame = "savescore.csv";
    final int N = 5; // entries in high score

    public LeaderBoard(PApplet p, Game game){
        this.p = p;
        this.game = game;

        // Leaderboard setup
        // letters for Input
        for (int i=0; i<letters.length; i++) {
            letters[i]='A';
        }
    }

    public void leaderBoardSetup(){
        //Check if there is a leaderboard already
        table = null;
        table = p.loadTable(savegame, "header");

        // If there isint then create one
        if (table!=null) {
            for (TableRow row : table.rows()) {
                int id = row.getInt("id");
            }
        } else {
            // fail:
            // first run, make table
            table = new Table();
            table.addColumn("id");
            table.addColumn("time");
            table.addColumn("name");
            for (int i = 0; i<N; i++) {
                TableRow newRow = table.addRow();
                newRow.setInt("id", table.lastRowIndex());
                newRow.setInt("time", 99999);
                newRow.setString("name", "");
            }
        }
    }

    public void printHighscores(){
        p.fill(0, 191, 255);
        p.textAlign(p.LEFT, p.CENTER);
        for (int i = 0; i < N; i++) {
            TableRow newRow = table.getRow(i);

            String x = Integer.toString(newRow.getInt("time"));
            //System.out.println(x.length());
            if(x.length() == 4){
                x = x.charAt(0) + "." + x.substring(1, x.length());
            } else {
                x = x.substring(0,2) + "." + x.substring(3, x.length());
            }

            p.text((i+1) + ". " + x
                    + " " + newRow.getString("name"), ( game.getWidth()/ 5) * 2, ((game.getHeight()/ 8) * 2) + 80 * i);
        }
    }

    void addNewScore(int score, String name) {

        TableRow newRow = table.addRow();
        newRow.setInt("id", table.lastRowIndex());
        newRow.setInt("time", (score));
        newRow.setString("name", name);

        // we sort
        table.setColumnType("time", Table.INT);
        table.trim();  // trim
        table.sort(1); // sort backwards by score

        // test
        p.println ("---");
        for (int i = 0; i<table.getRowCount(); i++) {
            newRow = table.getRow(i);
            p.println (newRow.getInt("time"), newRow.getString("name"));
        }
        p.println ("---");

        // we delete items
        if (table.getRowCount()>5)
            for (int i=table.getRowCount()-1; i>=5; i--) {
                TableRow row=table.getRow(i);
                table.removeRow(i);
            }

        // test
        p.println ("---");
        for (int i = 0; i<table.getRowCount(); i++) {
            newRow = table.getRow(i);
            p.println (newRow.getInt("time"), newRow.getString("name"));
        }
        p.println ("---");

        game.setGameState(10);
    }

    void saveScores() {
        p.saveTable(table, "data/savescore.csv");
    }

    boolean highEnoughForHighScore(int score) {

        // test whether new score is high enough to get into the highscore

        for (TableRow newRow : table.rows()) {
            //If the score is lower than the top 5

            if ((score < newRow.getInt("time"))) {
                return true; // high enough
            }
        }
        return false; // NOT high enough
    }

}
