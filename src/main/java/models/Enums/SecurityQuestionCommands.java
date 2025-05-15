package models.Enums;

public enum SecurityQuestionCommands {

    DREAM_JOB("1- What is your dream job?"),
    FAVORITE_TEACHER("2- What was the name of your favorite teacher in school?"),
    HISTORICAL_FIGURE("3- If you could have dinner with any historical figure, who would it be?"),
    FIRST_SCHOOL("4- What was your first school's name?"),
    FIRST_PHONE_MODEL("5- What was the model of your very first phone?");

    private final String question;

    SecurityQuestionCommands(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }


}

