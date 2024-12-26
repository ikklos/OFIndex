package site.sayaz.ofindex.data.remote

import android.content.Context
import android.net.Uri
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.tencent.cos.xml.transfer.COSXMLUploadTask
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.cos.xml.transfer.TransferState
import com.tencent.cos.xml.transfer.TransferStateListener
import com.tencent.qcloud.core.auth.QCloudCredentialProvider
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import site.sayaz.ofindex.data.remote.response.ImageUploadResponse


class ImageInstance(
    context: Context
) {

    // COS配置
    private val SECRET_ID = "AKIDADAtOegT1KPn8GAqOltnTu67wZ0FH7lK"
    private val SECRET_KEY = "RefT5VhDappb2bzAtVu65uVu8B0c2HME"
    private val BUCKET_NAME = "ofindex-1318773774"
    private val REGION = "ap-beijing"


    // 初始化COS客户端

    private var credentialProvider: QCloudCredentialProvider =
        ShortTimeCredentialProvider(SECRET_ID, SECRET_KEY, 3000)

    private var serviceConfig: CosXmlServiceConfig = CosXmlServiceConfig.Builder()
        .setRegion(REGION)
        .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
        .builder()

    private var cosXmlService = CosXmlService(context, serviceConfig, credentialProvider)

    // 初始化 TransferConfig
    val transferConfig = TransferConfig.Builder()
        .setDivisionForUpload(2097152) // 2MB
        .setSliceSizeForUpload(1048576) // 1MB
        .setForceSimpleUpload(true)
        .build()

    // 初始化 TransferManager
    val transferManager = TransferManager(cosXmlService, transferConfig)

    @OptIn(DelicateCoroutinesApi::class)
    val imageApiService = object : ImageApiService {
        override suspend fun uploadImage(uri: Uri): Deferred<Result<ImageUploadResponse>> {
            return GlobalScope.async(Dispatchers.IO) {
                try {
                    // 上传文件
                    val cosxmlUploadTask = transferManager.upload(BUCKET_NAME, uri.toString(), uri, null)

                    // 设置上传进度回调
                    cosxmlUploadTask.setCosXmlProgressListener { complete, target ->
                        println("Upload progress: $complete / $target")
                    }

                    // 等待任务完成并捕获结果
                    val result = CompletableDeferred<Result<ImageUploadResponse>>()

                    // 设置返回结果回调
                    cosxmlUploadTask.setCosXmlResultListener(object : CosXmlResultListener {
                        override fun onSuccess(xmlRequest: CosXmlRequest, xmlResult: CosXmlResult) {
                            val uploadResult = (xmlResult as COSXMLUploadTask.COSXMLUploadTaskResult)
                            val imageUrl = uploadResult.accessUrl
                            println("Upload successful, image URL: $imageUrl")
                            // 返回成功的响应
                            result.complete(Result.success(ImageUploadResponse(true, imageUrl)))
                        }

                        override fun onFail(
                            request: CosXmlRequest,
                            clientException: CosXmlClientException?,
                            serviceException: CosXmlServiceException?
                        ) {
                            if (clientException != null) {
                                clientException.printStackTrace()
                            } else serviceException?.printStackTrace()
                            // 返回失败的响应
                            result.complete(Result.failure(Exception("Upload failed")))
                        }
                    })

                    // 设置任务状态回调
                    cosxmlUploadTask.setTransferStateListener(object : TransferStateListener {
                        override fun onStateChanged(state: TransferState) {
                            println("Transfer state changed to: $state")
                        }
                    })

                    // 等待任务完成
                    result.await()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Result.failure(e)
                }
            }
        }
    }



}