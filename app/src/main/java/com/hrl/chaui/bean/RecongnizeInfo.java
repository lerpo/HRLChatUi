package com.hrl.chaui.bean;

public class RecongnizeInfo {
        private String RequestId;
        private String Result;
        private int AudioDuration;
        private int WordSize;

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String requestId) {
            RequestId = requestId;
        }

        public String getResult() {
            return Result;
        }

        public void setResult(String result) {
            Result = result;
        }

        public int getAudioDuration() {
            return AudioDuration;
        }

        public void setAudioDuration(int audioDuration) {
            AudioDuration = audioDuration;
        }

        public int getWordSize() {
            return WordSize;
        }

        public void setWordSize(int wordSize) {
            WordSize = wordSize;
        }
}
