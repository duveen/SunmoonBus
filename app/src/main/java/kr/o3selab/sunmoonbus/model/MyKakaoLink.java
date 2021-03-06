package kr.o3selab.sunmoonbus.model;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.kakaolink.internal.Action;
import com.kakao.util.KakaoParameterException;

import kr.o3selab.sunmoonbus.constant.ConstantsOld;
import kr.o3selab.sunmoonbus.R;

class MyKakaoLink extends KakaoLink {

    static void sendKakaoMessage(String location, String... args) {
        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(ConstantsOld.context);
            final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            StringBuilder message = new StringBuilder();
            message.append(ConstantsOld.context.getString(R.string.kakaolink_message_title)).append("\n");
            message.append(location).append("\n");
            for (int i = 0; i < args.length; i++) {
                if (i == args.length - 1)
                    message.append(args[i]);
                else
                    message.append(args[i]).append("\n");
            }

            kakaoTalkLinkMessageBuilder.addText(message.toString());
            kakaoTalkLinkMessageBuilder.addAppButton(ConstantsOld.context.getString(R.string.kakaolink_app_button), getAppAction("2.0"));

            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, ConstantsOld.context);
        } catch (Exception exception) {
            ConstantsOld.printLog(2, null, exception);
        }
    }

    private static Action getAppAction(String param) throws KakaoParameterException {
        AppActionBuilder appActionBuilder = new AppActionBuilder()
                .addActionInfo(
                        AppActionInfoBuilder
                                .createAndroidActionInfoBuilder()
                                .setExecuteParam("param=" + param)
                                .setMarketParam("referrer=kakaotalklink")
                                .build());

        return appActionBuilder.build();
    }
}
