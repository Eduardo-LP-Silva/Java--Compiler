class JVMTest {

    int UNDERPOP_LIM;
    int OVERPOP_LIM;
    int REPRODUCE_NUM;

    int LOOPS_PER_MS;

    int xMax;
    int yMax;
    int[] field;

    public static void main(String[] a) {
		JVMTest l;
		int unused;

		l = new JVMTest();
		l.init();

        while (true) {
            l.printField();
            l.update();
            unused = io.read();
        }

    }

    public boolean init() {
        int[] lineLenA;
        int lineLen;

        lineLenA = new int[1];

        /* "Static" variables */
        UNDERPOP_LIM = 2;
        OVERPOP_LIM = 3;
        REPRODUCE_NUM = 3;

        LOOPS_PER_MS = 225000;

        /* Instance variables */
        field = this.field(lineLenA);
        lineLen = lineLenA[0];

        xMax = lineLen - 1;
        yMax = field.length / lineLen - 1;

        return true;
    }

    /* Change this! 
     * (might want to write a script 
     * to autogenerate every assignment...)
     *
     * lineLen is "pass by reference",
     * and we modify it to return (e.g. time_t *time)
     */
    public int[] field(int[] lineLen) {
        int[] field;

        field = new int[100];
        lineLen[0] = 10;

        field[0] = 0;
        field[1] = 0;
        field[2] = 1;
        field[3] = 0;
        field[4] = 0;
        field[5] = 0;
        field[6] = 0;
        field[7] = 0;
        field[8] = 0;
        field[9] = 0;
        field[10] = 1;
        field[11] = 0;
        field[12] = 1;
        field[13] = 0;
        field[14] = 0;
        field[15] = 0;
        field[16] = 0;
        field[17] = 0;
        field[18] = 0;
        field[19] = 0;
        field[20] = 0;
        field[21] = 1;
        field[22] = 1;
        field[23] = 0;
        field[24] = 0;
        field[25] = 0;
        field[26] = 0;
        field[27] = 0;
        field[28] = 0;
        field[29] = 0;
        field[30] = 0;
        field[31] = 0;
        field[32] = 0;
        field[33] = 0;
        field[34] = 0;
        field[35] = 0;
        field[36] = 0;
        field[37] = 0;
        field[38] = 0;
        field[39] = 0;
        field[40] = 0;
        field[41] = 0;
        field[42] = 0;
        field[43] = 0;
        field[44] = 0;
        field[45] = 0;
        field[46] = 0;
        field[47] = 0;
        field[48] = 0;
        field[49] = 0;
        field[50] = 0;
        field[51] = 0;
        field[52] = 0;
        field[53] = 0;
        field[54] = 0;
        field[55] = 0;
        field[56] = 0;
        field[57] = 0;
        field[58] = 0;
        field[59] = 0;
        field[60] = 0;
        field[61] = 0;
        field[62] = 0;
        field[63] = 0;
        field[64] = 0;
        field[65] = 0;
        field[66] = 0;
        field[67] = 0;
        field[68] = 0;
        field[69] = 0;
        field[70] = 0;
        field[71] = 0;
        field[72] = 0;
        field[73] = 0;
        field[74] = 0;
        field[75] = 0;
        field[76] = 0;
        field[77] = 0;
        field[78] = 0;
        field[79] = 0;
        field[80] = 0;
        field[81] = 0;
        field[82] = 0;
        field[83] = 0;
        field[84] = 0;
        field[85] = 0;
        field[86] = 0;
        field[87] = 0;
        field[88] = 0;
        field[89] = 0;
        field[90] = 0;
        field[91] = 0;
        field[92] = 0;
        field[93] = 0;
        field[94] = 0;
        field[95] = 0;
        field[96] = 0;
        field[97] = 0;
        field[98] = 0;
        field[99] = 0;

        return field;

    }

    public boolean update() {
        int i;
        int cur;
        int neighN;
        boolean goodPop;
        int[] newField;

        newField = new int[field.length];

        i = 0;
        while (i < field.length) {
            cur = field[i];
            neighN = this.getLiveNeighborN(i);
            

            // Live cell
            if (!(cur < 1)) {
                goodPop = this.ge(neighN,UNDERPOP_LIM) && this.le(neighN,OVERPOP_LIM);
                if (!goodPop) {
                    newField[i] = 0;
                } else {
                    newField[i] = field[i];
                }
            }
            // Dead cell
            else {
                if (this.eq(neighN,REPRODUCE_NUM)) {
                    newField[i] = 1;
                } else {
                    newField[i] = field[i];
                }
            }

            i = i + 1;
        }

        field = newField;
        return true;

    }

    public boolean printField() {

        int i;
        int j;

        i = 0;
        j = 0;
        while (i < field.length) {
            if (this.gt(j,xMax)) {
                io.println();
                j = 0;
            }
            else {}
            io.print(field[i]);

            i = i + 1;
            j = j + 1;
        }

        io.println();
        io.println();
        return true;

    }

    public int trIdx(int x, int y) {
        return x + (xMax + 1) * y;
    }

    public int[] cartIdx(int absPos) {
        int x;
        int y;
        int xLim;
        int[] ret;

        xLim = xMax + 1;

        y = absPos / xLim;
        x = absPos - y * xLim;

        ret = new int[2];
        ret[0] = x;
        ret[1] = y;

        return ret;

    }

    public int[] getNeighborCoords(int absPos) {
        int x;
        int y;

        int upX;
        int upY;
        int downX;
        int downY;

        int[] cart;
        int[] ret;

        cart = this.cartIdx(absPos);
        x = cart[0];
        y = cart[1];

        if (x < xMax) {
            downX = x + 1;
            if (this.gt(x,0))
                upX = x - 1;
            else
                upX = xMax;
        } else {
            downX = 0;
            upX = x - 1;
        }

        if (y < yMax) {
            downY = y + 1;
            if (this.gt(y,0))
                upY = y - 1;
            else
                upY = yMax;
        } else {
            downY = 0;
            upY = y - 1;
        }

        ret = new int[8];
        // Clockwise from N
        ret[0] = this.trIdx(x, upY);
        ret[1] = this.trIdx(upX, upY);
        ret[2] = this.trIdx(upX, y);
        ret[3] = this.trIdx(upX, downY);
        ret[4] = this.trIdx(x, downY);
        ret[5] = this.trIdx(downX, downY);
        ret[6] = this.trIdx(downX, y);
        ret[7] = this.trIdx(downX, upY);

        return ret;

    }

    public int getLiveNeighborN(int absPos) {
        int[] neigh;
        int i;
        int ret;

        ret = 0;

        neigh = this.getNeighborCoords(absPos);

        i = 0;
        while (i < neigh.length) {
            if (this.ne(field[neigh[i]],0))
                ret = ret + 1;
            else {
            }

            i = i + 1;
        }

        return ret;
    }

    public boolean busyWait(int ms) {
        int i;
        int n;
        
        n = ms * LOOPS_PER_MS;
        
        // Try optimizing this away!
        i = 0;
        while (i < n) {
            i = i + 1;
        }
        
        return true;
    }
    
    public boolean eq(int a, int b) {
		return (!this.lt(a, b) && !this.lt(b, a));
	}
	
	public boolean ne(int a, int b) {
		return (!this.eq(a, b));
	}
	
    public boolean lt(int a, int b) {
		return (a < b);
    }
    
    public boolean le(int a, int b) {
		return !(!this.lt(a, b) && !this.eq(a, b));
    }
    
    public boolean gt(int a, int b) {
		return (!this.le(a, b));
    }
    
    public boolean ge(int a, int b) {
		return !(!this.gt(a, b) && !this.eq(a, b));
	}

}
