package models.Enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum SignupMenuCommands {


    Register("register\\s+-u\\s+(?<username>.*?)\\s+-p\\s+(?:(?<password>\\S+)\\s+(?<passwordConfirm>\\S+)|(?<random>random))\\s+-n\\s+(?<nickName>.*?)\\s+-e\\s+(?<email>.*?)\\s+-g\\s+(?<gender>\\S+)"),
    UserName("[a-zA-Z0-9-]+"),
    Password("[a-zA-Z0-9?<>,\"';:/|}\\{+)=*&^%$#!]+"),
    WeakPassword("(?=.{8,})(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[?<>,\"';:\\/|}\\{+\\)=*&^%$#!]).*"),
    Email("(?=.{1,64}@)([a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._-]{0,62}[a-zA-Z0-9])@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9](?:\\.[a-zA-Z]{2,})+)"),
    Gender("(male|female)"),

    SetRandomPassword("yes"),
    GetAnotherRandomPassword("random"),
    CancelGetRandomPassword("no"),

    SecurityQuestion("pick\\s+question\\s+-q\\s+(?<questionNumber>\\S+)\\s+-a\\s+(?<answer>.*?)\\s+-c\\s+(?<answerConfirm>.*?)"),

    ShowCurrentManu("show\\s+current\\s+menu"),

    GoToLoginMenu("menu\\s+enter\\s+login"),

    ExitMenu("exit"),

    ;

    private final String pattern;

    SignupMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMatcher(String input) {
        Matcher matcher = Pattern.compile(pattern).matcher(input);

        if (matcher.matches()) {
            return matcher;
        }
        return null;
    }

}

// register    -u   mostafa   -p paSS12#$   paSS12#$  -n mosi  -e mostafa@gmail.com -g male
// register   -u ahmad -p paSS12#$ paSS12#$ -n ahi -e ahamd@gmail.com -g male
// register -u reza -p paSS12#$ paSS12#$ -n reza -e reza@gmail.com -g male
// register -u mohsen -p paSS12#$ paSS12#$ -n mohsen -e mohsen@gmail.com -g male
//  register    -u   mostafa   -p random  -n mosi  -e mostafa@gmail.com -g male
// pick question -q 1 -a salam -c salam
// login -u mostafa -p paSS12#$
