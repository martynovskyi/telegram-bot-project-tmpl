package demo.project.main;

import demo.project.TestObjects;
import demo.project.component.PrettyJsonLogger;
import demo.project.component.TgChannelLogger;
import demo.project.config.properties.ApplicationProperties;
import com.motokyi.tg.bot_api.api.method.DeleteWebhook;
import com.motokyi.tg.bot_api.api.method.GetUpdates;
import com.motokyi.tg.bot_api.api.method.SetWebhook;
import com.motokyi.tg.bot_api.api.type.Response;
import com.motokyi.tg.bot_api.api.type.update.Update;
import com.motokyi.tg.bot_api.api.type.update.WebhookInfo;
import com.motokyi.tg.bot_api.bot.Bot;
import com.motokyi.tg.bot_api.tools.UpdateHandler;
import demo.project.bot.main.MainBotUpdatesListener;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MainBotUpdatesListenerTest {

    @Mock
    Bot bot;
    @Mock
    GetUpdates getUpdates;
    @Mock
    ApplicationProperties applicationProperties;
    @Mock
    UpdateHandler updateHandler;
    @Mock
    TgChannelLogger channelLogger;
    @Mock
    PrettyJsonLogger bodyLogger;

    MainBotUpdatesListener mainBotUpdatesListener;

    @NotNull
    private static Response<Boolean> deleteWebhookResponse() {
        Response<Boolean> booleanResponse = new Response<>();
        booleanResponse.setResult(Boolean.TRUE);
        booleanResponse.setOk(true);
        return booleanResponse;
    }

    @NotNull
    private static Response<WebhookInfo> getWebhookInfoResponse() {
        Response<WebhookInfo> webhookInfoResponse = new Response<>();
        webhookInfoResponse.setOk(true);
        WebhookInfo webhookInfo = new WebhookInfo();
        webhookInfo.setUrl("another_webhook");
        webhookInfoResponse.setResult(webhookInfo);
        return webhookInfoResponse;
    }

    @BeforeEach
    void setUp() {
        mainBotUpdatesListener = new MainBotUpdatesListener(applicationProperties,
                updateHandler,
                bodyLogger,
                channelLogger,
                TestObjects.context(bot));
    }

    @Test
    void onApplicationEvent_longPolling() {
        when(applicationProperties.isWebhookConfigured()).thenReturn(false);
        when(bot.getUpdates()).thenReturn(getUpdates);
        when(getUpdates.updateStream()).thenReturn(Flux.just(new Update()));

        mainBotUpdatesListener.onApplicationEvent(null);

        verify(applicationProperties).isWebhookConfigured();
        verify(bot).getUpdates();
        verify(updateHandler).apply(any());
        verifyNoMoreInteractions(applicationProperties, updateHandler, channelLogger);
    }

    @Test
    void onApplicationEvent_webhook() {
        when(applicationProperties.isWebhookConfigured()).thenReturn(true);
        when(applicationProperties.getWebhookUrl()).thenReturn("webhook");
        Response<WebhookInfo> webhookInfoResponse = getWebhookInfoResponse();
        when(bot.getWebhookInfo()).thenReturn(Mono.just(webhookInfoResponse));
        DeleteWebhook deleteWebhook = mock(DeleteWebhook.class);
        when(deleteWebhook.send()).thenReturn(Mono.just(deleteWebhookResponse()));
        when(bot.deleteWebhook()).thenReturn(deleteWebhook);
        SetWebhook setWebhook = mock(SetWebhook.class);
        when(bot.setWebhook("webhook")).thenReturn(setWebhook);
        when(setWebhook.secretToken(any())).thenReturn(setWebhook);

        mainBotUpdatesListener.onApplicationEvent(null);


        verify(applicationProperties).isWebhookConfigured();
        verify(applicationProperties, times(2)).getWebhookUrl();
        verify(applicationProperties).getWebhookToken();
        verify(bot).getWebhookInfo();
        verify(bot).deleteWebhook();
        verify(bot).setWebhook(any());
        verifyNoMoreInteractions(applicationProperties, updateHandler, channelLogger);
    }

}