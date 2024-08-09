import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.smhrd.bookor.DataPart
import java.io.ByteArrayOutputStream
import java.io.IOException

open class VolleyMultipartRequest(
    method: Int,
    url: String,
    private val mListener: Response.Listener<NetworkResponse>,
    private val mErrorListener: Response.ErrorListener
) : Request<NetworkResponse>(method, url, mErrorListener) {

    private var responseHeaders: Map<String, String>? = null

    override fun getHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "multipart/form-data;boundary=$boundary"
        return headers
    }

    override fun getBodyContentType(): String {
        return "multipart/form-data;boundary=$boundary"
    }

    override fun getBody(): ByteArray {
        val bos = ByteArrayOutputStream()
        try {
            val params = getParams() ?: emptyMap() // null 확인
            for ((key, value) in params.entries) {
                buildTextPart(bos, key, value)
            }
            for ((key, value) in getByteData().entries) {
                buildFilePart(bos, key, value)
            }
            bos.write(("--$boundary--\r\n").toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bos.toByteArray()
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<NetworkResponse> {
        responseHeaders = response.headers
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response))
    }

    override fun deliverResponse(response: NetworkResponse) {
        mListener.onResponse(response)
    }

    override fun deliverError(error: VolleyError) {
        mErrorListener.onErrorResponse(error)
    }

    open fun getByteData(): Map<String, DataPart> {
        return HashMap()
    }

    @Throws(IOException::class)
    private fun buildTextPart(dataOutputStream: ByteArrayOutputStream, key: String, value: String) {
        dataOutputStream.write(("--$boundary\r\n").toByteArray())
        dataOutputStream.write("Content-Disposition: form-data; name=\"$key\"\r\n".toByteArray())
        dataOutputStream.write("\r\n".toByteArray())
        dataOutputStream.write("$value\r\n".toByteArray())
    }

    @Throws(IOException::class)
    private fun buildFilePart(dataOutputStream: ByteArrayOutputStream, key: String, dataFile: DataPart) {
        dataOutputStream.write(("--$boundary\r\n").toByteArray())
        dataOutputStream.write(("Content-Disposition: form-data; name=\"$key\"; filename=\"${dataFile.fileName}\"\r\n").toByteArray())
        if (dataFile.type.isNotEmpty()) {
            dataOutputStream.write(("Content-Type: ${dataFile.type}\r\n").toByteArray())
        }
        dataOutputStream.write("\r\n".toByteArray())
        dataOutputStream.write(dataFile.content)
        dataOutputStream.write("\r\n".toByteArray())
    }

    companion object {
        private const val boundary = "volleyBoundary"
    }
}
