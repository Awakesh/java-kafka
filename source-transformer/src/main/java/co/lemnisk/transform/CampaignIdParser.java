package co.lemnisk.transform;

public class CampaignIdParser {

    public static CharSequence parse(String accountId) {
        if(accountId == null) return "";

        return accountId.replace("VIZVRM", "");
    }

    public static CharSequence parse(int accountId) {
        return String.valueOf(accountId);
    }
}