import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Ethan Petuchowski 9/21/13 */
public class Subject
{
    String name;

    private int column   = 0;
    int      overallTime = 0;
    String[] topTwo      = {"", ""};

    Map<String, Integer> tasks = new HashMap<>();

    Subject(String n) {
        name = n;
    }

    /*
     * add the data from one line of the Minco log to the task total
     * make a new task if necessary
     */
    public void addToTask(String task, int time) {
        overallTime += time;
        if (tasks.containsKey(task)) {
            int oldTime = tasks.get(task);
            tasks.put(task, oldTime + time);
        }
        else tasks.put(task, time);
    }

    /*
     * note the two tasks with most time spent on them
     */
    public void calcTopTwo() {
        int    max  = 0,  max2 = 0;
        String top  = "", top2 = "";
        for (String task : tasks.keySet()) {
            int taskTotal = tasks.get(task);
            if (max < taskTotal) {
                max2 = max;
                max  = taskTotal;
                top2 = top;
                top  = task;
            }
            else if (max2 < taskTotal) {
                max2 = taskTotal;
                top2 = task;
            }
        }
        topTwo[0] = top;
        topTwo[1] = top2;
    }

    public String biggest() {
        return topTwo[0];
    }

    public String secondBiggest() {
        return topTwo[1];
    }

    @Override
    public String toString() {
        return name;
    }

    public Boolean hasTasks() {
        return tasks.size() > 0;
    }

    public Boolean needsTwoColumns() {
        return tasks.size() > 1;
    }

    public double timeInHours() {
        return overallTime / 60.0;
    }

    public String timeString() {
        return String.format("%3.2f", timeInHours());
    }

    /*
     * Subject
     * -------
     */
    public String heading() {
        StringBuilder sb = new StringBuilder(name+'\n');
        for (int i = 0; i < name.length(); i++) {
            sb.append('-');
        }
        return sb.toString();
    }

    /*
     * Task1: 23
     * Task2: 25
     */
    public ArrayList<String> getTaskStrings() {
        ArrayList<String> taskStrings = new ArrayList<>();
        for (String task : tasks.keySet()) {
            int value = tasks.get(task);
            int hours = value / 60;
            int minutes = value % 60;
            String time = String.format("%d:%02d", hours, minutes);
            taskStrings.add(task + ": " + time);
        }
        return taskStrings;
    }

    /** PRINTS:
     *
     * Subject
     * -------
     * Task1: 23
     * Task2: 25
     * =========
     */
    public void printTaskTimes(String divider) {
        System.out.println(heading());
        if (this.hasTasks()) {
            for (String taskString : this.getTaskStrings())
                System.out.println(taskString);
            System.out.println(divider);
        }
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
