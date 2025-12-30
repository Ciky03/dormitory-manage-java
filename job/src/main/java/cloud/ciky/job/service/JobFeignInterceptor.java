package cloud.ciky.job.service;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobFeignInterceptor implements RequestInterceptor {

    private final AuthService tokenClient;

    @Override
    public void apply(RequestTemplate template) {
        String token = tokenClient.getAccessToken();
        template.header("Authorization", "Bearer " + token);
    }
}