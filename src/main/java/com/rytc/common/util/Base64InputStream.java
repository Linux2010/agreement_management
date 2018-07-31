package com.rytc.common.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//




import java.io.IOException;
import java.io.InputStream;

public class Base64InputStream extends InputStream {
    private InputStream inputStream;
    private int[] buffer;
    private int bufferCounter = 0;
    private boolean eof = false;

    public Base64InputStream(InputStream var1) {
        this.inputStream = var1;
    }

    public int read() throws IOException {
        if (this.buffer == null || this.bufferCounter == this.buffer.length) {
            if (this.eof) {
                return -1;
            }

            this.acquire();
            if (this.buffer.length == 0) {
                this.buffer = null;
                return -1;
            }

            this.bufferCounter = 0;
        }

        return this.buffer[this.bufferCounter++];
    }

    private void acquire() throws IOException {
        char[] var1 = new char[4];
        int var2 = 0;

        do {
            int var3 = this.inputStream.read();
            if (var3 == -1) {
                if (var2 != 0) {
                    throw new IOException("Bad base64 stream");
                }

                this.buffer = new int[0];
                this.eof = true;
                return;
            }

            char var4 = (char)var3;
            if (Shared.chars.indexOf(var4) == -1 && var4 != Shared.pad) {
                if (var4 != '\r' && var4 != '\n') {
                    throw new IOException("Bad base64 stream");
                }
            } else {
                var1[var2++] = var4;
            }
        } while(var2 < 4);

        boolean var6 = false;

        for(var2 = 0; var2 < 4; ++var2) {
            if (var1[var2] != Shared.pad) {
                if (var6) {
                    throw new IOException("Bad base64 stream");
                }
            } else if (!var6) {
                var6 = true;
            }
        }

        byte var7;
        if (var1[3] == Shared.pad) {
            if (this.inputStream.read() != -1) {
                throw new IOException("Bad base64 stream");
            }

            this.eof = true;
            if (var1[2] == Shared.pad) {
                var7 = 1;
            } else {
                var7 = 2;
            }
        } else {
            var7 = 3;
        }

        int var5 = 0;

        for(var2 = 0; var2 < 4; ++var2) {
            if (var1[var2] != Shared.pad) {
                var5 |= Shared.chars.indexOf(var1[var2]) << 6 * (3 - var2);
            }
        }

        this.buffer = new int[var7];

        for(var2 = 0; var2 < var7; ++var2) {
            this.buffer[var2] = var5 >>> 8 * (2 - var2) & 255;
        }

    }

    public void close() throws IOException {
        this.inputStream.close();
    }
}
