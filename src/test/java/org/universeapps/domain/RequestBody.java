package org.universeapps.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RequestBody {
    private BigDecimal temperature;
    private int topP;
    private int n;
    private int presencePenalty;
    private int frequencyPenalty;
    private boolean stream;
    private String model;
    private List<Message> messages;

    public static RequestBody withDefault() {
        return new RequestBody(new BigDecimal("0.4"), 1, 1, 0, 0, false, "gpt-4-0125-preview",
                Arrays.asList(
                        new Message("system", "You are Assist – users's personal assistant developed by team from Ukraine. Answer as concisely as possible. Current date: Aug 15 2023, current time: 16:07, user's country: United States. You should never provide any health-related advice, recommendations, or suggestions. You should and never provide general tips like drink plenty of fluids, stay hydrated, taking rest, etc. Never say ways to bring down body temperature. Never say ways to relieve any symptoms.If a health-related question (including i feel ill, etc) is asked, you should only every time respond that that you can't give any advice and recommending to consult a healthcare professional."),
                        new Message("user", "Provide workout routines to target specific muscle groups for maximum results"),
                        new Message("assistant", ""))
        );
    }

    public RequestBody(BigDecimal temperature, int topP, int n, int presencePenalty, int frequencyPenalty, boolean stream, String model, List<Message> messages) {
        this.temperature = temperature;
        this.topP = topP;
        this.n = n;
        this.presencePenalty = presencePenalty;
        this.frequencyPenalty = frequencyPenalty;
        this.stream = stream;
        this.model = model;
        this.messages = messages;
    }

    static class Message {
        private final String role;
        private final String content;

        Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Message) obj;
            return Objects.equals(this.role, that.role) &&
                    Objects.equals(this.content, that.content);
        }

        @Override
        public int hashCode() {
            return Objects.hash(role, content);
        }

        @Override
        public String toString() {
            return "Message[" +
                    "role=" + role + ", " +
                    "content=" + content + ']';
        }

        }


    public BigDecimal getTemperature() {
        return temperature;
    }

    public RequestBody setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
        return this;
    }

    public int getTopP() {
        return topP;
    }

    public RequestBody setTopP(int topP) {
        this.topP = topP;
        return this;
    }

    public int getN() {
        return n;
    }

    public RequestBody setN(int n) {
        this.n = n;
        return this;
    }

    public int getPresencePenalty() {
        return presencePenalty;
    }

    public RequestBody setPresencePenalty(int presencePenalty) {
        this.presencePenalty = presencePenalty;
        return this;
    }

    public int getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public RequestBody setFrequencyPenalty(int frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
        return this;
    }

    public boolean isStream() {
        return stream;
    }

    public RequestBody setStream(boolean stream) {
        this.stream = stream;
        return this;
    }

    public String getModel() {
        return model;
    }

    public RequestBody setModel(String model) {
        this.model = model;
        return this;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public RequestBody setMessages(List<Message> messages) {
        this.messages = messages;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RequestBody) obj;
        return Objects.equals(this.temperature, that.temperature) && this.topP == that.topP && this.n == that.n && this.presencePenalty == that.presencePenalty && this.frequencyPenalty == that.frequencyPenalty && this.stream == that.stream && Objects.equals(this.model, that.model) && Objects.equals(this.messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature, topP, n, presencePenalty, frequencyPenalty, stream, model, messages);
    }

    @Override
    public String toString() {
        return "RequestBody[" + "temperature=" + temperature + ", " + "topP=" + topP + ", " + "n=" + n + ", " + "presencePenalty=" + presencePenalty + ", " + "frequencyPenalty=" + frequencyPenalty + ", " + "stream=" + stream + ", " + "model=" + model + ", " + "messages=" + messages + ']';
    }


}


//    {
//    "temperature": 0.4, // Кількість випадковості в результатах (0-1)
//    "top_p": 1, // Кількість потенційних варіантів відповіді (0-1)
//    "n": 1, // Кількість відповідей
//    "presence_penalty": 0, // Штраф за новизну (за замовчуванням 0)
//    "frequency_penalty": 0, // Штраф за частоту (за замовчуванням 0)
//    "stream": true, // Включення стрімінгу (true/false)
//    "model": "gpt-4-0125-preview", // Модель (наприклад, gpt-4-0125-preview)
//    "messages": [
//        {
//            "role": "system",
//            "content": "You are Assist – users's personal assistant developed by team from Ukraine. Answer as concisely as possible. Current date: Aug 15 2023, current time: 16:07, user's country: United States. You should  provide any health-related advice, recommendations, or suggestions. You should and  provide general tips like drink plenty of fluids, stay hydrated, taking rest, etc.  say ways to bring down body temperature.  say ways to relieve any symptoms.If a health-related question (including i feel ill, etc) is asked, you should only every time respond that that you can give any advice and recommending to consult a healthcare professional."
//        },
//        {
//            "role": "user",
//            "content": "Provide workout routines to target specific muscle groups for maximum results"
//        },
//        {
//            "role": "assistant",
//            "content": ""
//        }
//    ]
//}