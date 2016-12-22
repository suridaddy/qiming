package com.yxb.names.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.UnsupportedCharsetException;

/**
 * A wrapper make Writer as OutputStream.
 */
public class WriterOutputStream extends OutputStream {
    public static final String COPYRIGHT = "(C) Copyright IBM Corporation 2009, 2015. All Rights Reserved.";//$NON-NLS-1$
    final static int CHAR_BUFFER_SIZE = 1024;
    private Writer writer;
    private CharsetDecoder decoder;
    // end of output byte stream flag
    private boolean outputClosed = false;

    private char[] chars;
    private CharBuffer charBuf;
    private ByteBuffer byteBuf;

    /**
     * Contruct a wrapper from a writer and encoding.
     * 
     * @param writer
     * @param encoding
     */
    public WriterOutputStream(Writer writer, String encoding)
            throws UnsupportedEncodingException {
        if (writer == null) {
            throw new IllegalArgumentException("writer cannot be null");
        }

        this.writer = writer;

        Charset charset = getCharset(encoding);
        decoder = charset.newDecoder();

        this.chars = new char[CHAR_BUFFER_SIZE];
        this.charBuf = CharBuffer.wrap(chars);

        int size = (int) (chars.length / decoder.averageCharsPerByte());
        size -= size % 8; // align boundary to 8
        this.byteBuf = ByteBuffer.allocate(size);
    }

    @Override
    public void write(int b) throws IOException {
        if (outputClosed) {
            throw new IOException("this stream is already been closed.");
        }

        byteBuf.put((byte) b);

        decode(false);
    }

    @Override
    public void flush() throws IOException {
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        if (outputClosed)
            return;

        outputClosed = true;
        decode(true);

        CoderResult cr = decoder.flush(charBuf);
        if (cr.isError()) {
            throw new IOException("Error occurs while decoding byte stream: "
                    + cr.toString());
        }

        output();

        flush();
        // TODO close underlying writer?
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0)
                || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }

        if (outputClosed) {
            throw new IOException("this stream is already been closed.");
        }

        ByteBuffer buf = ByteBuffer.wrap(b, off, len);
        while (buf.remaining() > 0) {
            copy(buf, byteBuf);
            decode(false);
        }
    }

    /**
     * Copy bytes from source to destination as much as possible. The position
     * of both buffers will be advanced.
     * 
     * @param src
     *            the source buffer, should not be read only buffer
     * @param dst
     *            the destination buffer
     */
    static void copy(ByteBuffer src, ByteBuffer dst) {
        int n = src.remaining() <= dst.remaining() ? src.remaining() : dst
                .remaining();
        // We assume the arrayOffset = 0 for both byte buffers
        System.arraycopy(src.array(), src.position(), dst.array(),
                dst.position(), n);

        src.position(src.position() + n);
        dst.position(dst.position() + n);
    }

    private void decode(boolean endOfInput) throws IOException {
        CoderResult cr;

        byteBuf.flip();
        do {
            cr = decoder.decode(byteBuf, charBuf, endOfInput);
            output();
        } while (cr.isOverflow());
        byteBuf.compact();
        // 697588
        if (cr.isMalformed()) {
            throw new IOException(
                    "Error occurs while decoding byte stream: The input is Malformed. " + cr.toString());//$NON-NLS-$1

        }

        if (cr.isUnmappable()) {
            throw new IOException(
                    "Error occurs while decoding byte stream: The unmappable-character error. "
                            + cr.toString());
        }
    }

    /**
     * Output the content of charBuf to writer
     * 
     * @throws IOException
     */
    private void output() throws IOException {
        charBuf.flip();

        // Judge whether charBuf is empty. If not, output the content of charBuf
        if (charBuf.remaining() > 0) {
            writer.write(chars, 0, charBuf.limit());
        }

        charBuf.clear();
    }

    /**
     * Get Charset of specified encoding name.
     * 
     * @param encoding
     *            the encoding name.
     * @return the Charset instance.
     * @throws UnsupportedEncodingException
     *             if the encoding is not supported by JVM.
     */
    static Charset getCharset(String encoding)
            throws UnsupportedEncodingException {
        if (encoding == null) {
            return Charset.defaultCharset();
        }

        try {
            return Charset.forName(encoding);
        } catch (UnsupportedCharsetException e) {
            throw new UnsupportedEncodingException(encoding);
        }
    }
}
