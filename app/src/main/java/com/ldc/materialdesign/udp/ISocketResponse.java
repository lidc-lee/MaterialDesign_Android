package com.ldc.materialdesign.udp;

/**
 * Created by LinJK on 7/2/16.
 */
public interface ISocketResponse {
    /**
     * On socket response.
     *
     * @param responseData the response data, you should implement it to receive data.
     */
    void onSocketResponse(byte[] responseData);
}
