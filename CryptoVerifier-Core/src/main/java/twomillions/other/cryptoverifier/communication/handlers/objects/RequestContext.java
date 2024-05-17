package twomillions.other.cryptoverifier.communication.handlers.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.communication.ValidationInfoData;
import twomillions.other.cryptoverifier.communication.data.SensitiveData;
import twomillions.other.cryptoverifier.communication.enums.RequestHeader;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.KeyPair;

@Getter
@Setter
@Accessors(chain = true)
public class RequestContext {
    private Socket socket;
    private ValidationInfoData validationInfoData;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    private KeyPair serverKeyPair;
    private SensitiveData sensitiveData;

    private RequestHeader clientRequestDataHeader;
    private String clientRequestDataMessage;
}
