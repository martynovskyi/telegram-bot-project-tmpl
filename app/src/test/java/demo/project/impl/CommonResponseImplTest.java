package demo.project.impl;

import demo.project.constant.Texts;
import com.motokyi.tg.bot_api.api.method.SendMessage;
import com.motokyi.tg.bot_api.api.type.chat.Chat;
import com.motokyi.tg.bot_api.api.type.message.Message;
import com.motokyi.tg.bot_api.bot.Bot;
import demo.project.service.impl.CommonResponseImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonResponseImplTest {
    @Mock
    Bot bot;

    @Mock(answer = Answers.RETURNS_SELF)
    SendMessage sendMessage;

    @InjectMocks
    CommonResponseImpl commonResponse;

    @Test
    void setupNoWayToHandle() {
        Message message = new Message();
        Chat chat = new Chat();
        message.setChat(chat);
        when(bot.sendMessage(any(Chat.class))).thenReturn(sendMessage);

        commonResponse.noWayToHandle(bot, message);

        verify(bot).sendMessage(chat);
        verify(sendMessage).text(Texts.I_DONT_KNOW_HOW_HANDLE);
        verify(sendMessage).send();
        verifyNoMoreInteractions(bot, sendMessage);
    }
}