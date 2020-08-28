package com.vonage.tutorial.voice;

public class Config {

    public static User getAlice() {
        return new User(
                "Alice",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1OTg1MzY2MjAsImp0aSI6IjJkODkxN2UwLWU4NmQtMTFlYS05YjZlLTQ3YjllZjhmZTNlNCIsImV4cCI6MTU5ODYyMzAxOSwiYWNsIjp7InBhdGhzIjp7Ii8qL3VzZXJzLyoqIjp7fSwiLyovY29udmVyc2F0aW9ucy8qKiI6e30sIi8qL3Nlc3Npb25zLyoqIjp7fSwiLyovZGV2aWNlcy8qKiI6e30sIi8qL2ltYWdlLyoqIjp7fSwiLyovbWVkaWEvKioiOnt9LCIvKi9hcHBsaWNhdGlvbnMvKioiOnt9LCIvKi9wdXNoLyoqIjp7fSwiLyova25vY2tpbmcvKioiOnt9fX0sInN1YiI6IkFsaWNlIiwiYXBwbGljYXRpb25faWQiOiJmNTVhZGU1Zi0zY2Y4LTQ5MzAtYTliZC04MDYwYjRmMTU3ZTgifQ.iiUVHGrCcPmOi_7Hgh2RNY5OA3P08GsQ9gnAFWjIpzjt30b4XY_NWUV4eu8ZfHUhJpuddcD8L_BpfCTHNg-eKyHTF8fCqMVDPId91sYzZmzHovkxTSVl-lkJx9ttvlXiw-njou161MuDC5f72lZVyFHaWGRzMllsWv3qg6_jxzs-W9Erkjj8AQHjEVo_uPmXN_Miy2eFJD_4NvHecpNrqCyPf1_8_ZSJP6vm7fJ1fnE9TUPjX6nCIFYTF5VS-csRzybqmLxUdNoAGx64hhk9wtXgzjel8bNZLai7s323nsyA3CTguSy-RD-e8sqXIz2Ofycn9gDYGL-RdzmcLjCCww" // TODO: "set Bob JWT token"
        );
    }

    public static User getBob() {
        return new User(
                "Bob",
                "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE1OTg1MzY2NDQsImp0aSI6IjNiZTJjY2EwLWU4NmQtMTFlYS04ZmUxLTU5MDU3ZjY2NDU5ZCIsImV4cCI6MTU5ODYyMzA0MywiYWNsIjp7InBhdGhzIjp7Ii8qL3VzZXJzLyoqIjp7fSwiLyovY29udmVyc2F0aW9ucy8qKiI6e30sIi8qL3Nlc3Npb25zLyoqIjp7fSwiLyovZGV2aWNlcy8qKiI6e30sIi8qL2ltYWdlLyoqIjp7fSwiLyovbWVkaWEvKioiOnt9LCIvKi9hcHBsaWNhdGlvbnMvKioiOnt9LCIvKi9wdXNoLyoqIjp7fSwiLyova25vY2tpbmcvKioiOnt9fX0sInN1YiI6IkJvYiIsImFwcGxpY2F0aW9uX2lkIjoiZjU1YWRlNWYtM2NmOC00OTMwLWE5YmQtODA2MGI0ZjE1N2U4In0.a8A_nppLU1o1TZejFht6YUKnMj7B0NVoMDk0OVpYmrqQoBl79lFuMb1q-O90JkP0jA5OXu1VIIIr7qM_wup2w7VgsILD6bv6HRNaKXHuu2KR5P_jkOmXKYCQbbyk3vlak3MuUGRUFpq--a5kfNAZJtm78N7dJ-ddYzHeDynh2R9DaU4QnlE5n8FyOZm-GhCCi11kbiyHoqbDb_Ao-ai7esggmd4CMCeDh1uInJ-2Sfq1Aeno_uYbzd2t_dPTymrZllAen30whTO_K8XwldJudmr16Fs958rv_jYNl6pJ3lOx81P1Kw15RjeIULLSteHqlPxyRzPz0HwcaBIPCRVXJQ" // TODO: "set Bob JWT token"
        );
    }

    public static String getOtherUserName(String userName) {
        if (userName.equals(getAlice().getName())) {
            return getBob().getName();
        } else {
            return getAlice().getName();
        }
    }
}
