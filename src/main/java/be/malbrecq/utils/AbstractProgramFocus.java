package be.malbrecq.utils;

public abstract class AbstractProgramFocus implements ProgramFocus {

    protected abstract String getName();
    protected abstract String getLaunchCommand();

    public void launch() {
        if (isLaunched()) {
            focus();
        } else {
            CommandLauncher.Command(getLaunchCommand(), false);
        }
    }

    public void quit() {
        CommandLauncher.Command("wmctrl -c " + getName());
    }

    public void focus() {
        CommandLauncher.Command("wmctrl -a " + getName());
    }

    protected Boolean isLaunched() {
        return CommandLauncher.Command("wmctrl -l " + getName()).stream().anyMatch(line -> line.contains(getName()));
    }
}
