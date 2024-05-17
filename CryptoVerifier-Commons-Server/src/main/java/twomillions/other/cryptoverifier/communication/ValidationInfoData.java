package twomillions.other.cryptoverifier.communication;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@SuppressWarnings("unused")
public class ValidationInfoData {
    private String ip;
    private String uuid;
    private String result;
    private String message;

    private long endTime;
    private long startTime;

    public long getElapsedTime() {
        return endTime - startTime;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public static ValidationInfoData fromString(String string) {
        return new GsonBuilder().create().fromJson(string, ValidationInfoData.class);
    }
}
