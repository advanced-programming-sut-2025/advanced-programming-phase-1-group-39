package models.Enums;

public enum SecurityQuestionCommands {

    DREAM_JOB("What is your dream job?"),
    FAVORITE_TEACHER("What was the name of your favorite teacher in school?"),
    HISTORICAL_FIGURE("If you could have dinner with any historical figure, who would it be?"),
    FIRST_SCHOOL("What was your first school's name?"),
    FIRST_PHONE_MODEL("What was the model of your very first phone?");

    private final String question;

    SecurityQuestionCommands(String question) {
        this.question = question;
    }

}

