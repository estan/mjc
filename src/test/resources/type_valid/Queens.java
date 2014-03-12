/**
   Generate, print and count all possible configurations of
   eight queens on a chessboard.
   @author Lars Engebretsen
*/
class Queens
{
    public static void main(String[] a) {
	System.out.println(new Q().go());
    }
}

class Q {
    int numberOfLayouts;

    public int go() {
	int r_;
	Board empty;
	numberOfLayouts = 0;
	empty = new Board();
	r_ = empty.init();
	r_ = this.next(empty, 1);
	return numberOfLayouts;
    }

    public int next(Board currentBoard, int nextRow) {
	Board newBoard;
	int nextCol;
	int i;
	int r_;

	if(nextRow < 9) {
	    nextCol = 1;
	    while(nextCol < 9) {
		if(this.ok(currentBoard, nextRow, nextCol)) {
		    newBoard = currentBoard.dup();
		    r_ = newBoard.add(nextRow, nextCol);
		    r_ = this.next(newBoard, nextRow + 1);
		}
		else {}
		nextCol = nextCol + 1;
	    }
	}
	else {
	    r_ = currentBoard.print();
	    numberOfLayouts = numberOfLayouts + 1;
	}
	return 0;
    }

    public boolean ok(Board board, int addRow, int addCol) {
	int cLeft;
	int cRight;
	boolean retval;
	retval = true;
	cLeft = addCol;
	cRight = addCol;

	while(1 < addRow) {
	    addRow = addRow - 1;
	    cLeft = cLeft - 1;
	    cRight = cRight + 1;
	    if(this.equal(board.getCol(addRow), addCol))
		retval = false;
	    else if(0 < cLeft && this.equal(board.getCol(addRow), cLeft))
		retval = false;
	    else if(cRight < 9 && this.equal(board.getCol(addRow), cRight))
		retval = false;
	    else {}
	}
	return retval;
    }

    public boolean equal(int a, int b) {
	return !(a < b) && !(b < a);
    }
}

class Board {
    int[] rows;

    public int add(int row, int col) {
	rows[row-1] = col;
	return 0;
    }

    public int getCol(int row) {
	return rows[row-1];
    }

    public Board dup() {
	Board b;
	int r;
	int r_;
	b = new Board();
	r_ = b.init();
	r = 1;
	while(r < 9) {
	    r_ = b.add(r, rows[r-1]);
	    r = r+1;
	}
	return b;
    }

    public int init() {
	rows = new int[8];
	return 0;
    }

    public int print() {
	int row;
	int col;
	int mask;
	System.out.println(888888888);
	row = 0;
	while(row < 8) {
	    col = rows[row];
	    mask = 0;
	    if(0 < col) {
		mask = 1;
		col = 8 - col;
		while(0 < col) {
		    mask = mask * 10;
		    col = col - 1;
		}
	    }
	    else {}
	    System.out.println(800000000 + mask);
	    row = row + 1;
	}
	System.out.println(888888888);
	System.out.println(1);
	System.out.println(1);
	System.out.println(1);
	return 0;
    }
}
