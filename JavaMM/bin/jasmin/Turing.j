.class public Turing
.super java/lang/Object

.field 'NUM_SYMBOLS' I
.field 'curPos' I
.field 'MTABLE' [I
.field 'TAPE' [I
.field 'WTABLE' [I
.field 'R' I
.field 'NUM_STATES' I
.field 'curState' I
.field 'NTABLE' [I
.field 'L' I
.field 'H' I

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 999
	.limit locals 2

	new Turing
	dup
	invokenonvirtual Turing/<init>()V
	astore 1

	aload 1
	invokevirtual error/init_bb_3s2sy()V
	aload 1
	invokevirtual error/run()V
	return
.end method

.method public init_bb_3s2sy()Z
	.limit stack 999
	.limit locals 1

	aload_0
	aload_0
	iconst_3
	iconst_2
	bipush 18
	invokevirtual Turing/initGeneric(III)[I
	putfield Turing/TAPE [I

	aload_0
	iconst_0
	iconst_0
	iconst_1
	aload_0
	getfield Turing/R I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_1
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_0
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_2
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_0
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_2
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_1
	iconst_1
	aload_0
	getfield Turing/R I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_2
	iconst_1
	aload_0
	getfield Turing/R I
	aload_0
	getfield Turing/H I
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	iconst_1
	ireturn
.end method

.method public init_bb_4s2sy()Z
	.limit stack 999
	.limit locals 1

	aload_0
	aload_0
	iconst_4
	iconst_2
	bipush 20
	invokevirtual Turing/initGeneric(III)[I
	putfield Turing/TAPE [I

	aload_0
	iconst_0
	iconst_0
	iconst_1
	aload_0
	getfield Turing/R I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_1
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_0
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_2
	iconst_1
	aload_0
	getfield Turing/R I
	aload_0
	getfield Turing/H I
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_0
	iconst_3
	iconst_1
	aload_0
	getfield Turing/R I
	iconst_3
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_0
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_1
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_1
	iconst_0
	aload_0
	getfield Turing/L I
	iconst_2
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_2
	iconst_1
	aload_0
	getfield Turing/L I
	iconst_3
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	aload_0
	iconst_1
	iconst_3
	iconst_0
	aload_0
	getfield Turing/R I
	iconst_0
	invokevirtual Turing/setTrans(IIIII)Z
	pop
	iconst_1
	ireturn
.end method

.method public run()Z
	.limit stack 999
	.limit locals 4

	aload_0
	astore 3

	iconst_0
	istore 2


run(0)1:
	iload 2
	ifne run(0)2
	aload_0
	invokevirtual Turing/printTape()Z
	pop
	invokestatic io/read()error
	istore 1

	aload 3
	invokevirtual error/trans()V
	ifne run(0)3
	iconst_1
	goto run(0)4

run(0)3:
	iconst_0

run(0)4:
	istore 2

	goto run(0)1

run(0)2:
	aload_0
	invokevirtual Turing/printTape()Z
	pop
	iconst_1
	ireturn
.end method

.method public printTape()Z
	.limit stack 999
	.limit locals 2

	iconst_0
	istore 1


printTape(0)1:
	iload 1
	aload_0
	getfield Turing/TAPE [I
	arraylength
	if_icmpge printTape(0)2
	iload 1
	aload_0
	getfield Turing/curPos I
	if_icmplt printTape(0)4
	ifne printTape(0)4
	aload_0
	getfield Turing/curPos I
	iload 1
	if_icmplt printTape(0)4
	ifne printTape(0)4
	iconst_0
	invokestatic io/print(I)V
	goto printTape(0)3

printTape(0)4:
	aload_0
	getfield Turing/curState I
	iconst_1
	iadd
	invokestatic io/print(error)V

printTape(0)3:
	iload 1
	iconst_1
	iadd
	istore 1

	goto printTape(0)1

printTape(0)2:
	invokestatic io/println()V
	iconst_0
	istore 1


printTape(0)5:
	iload 1
	aload_0
	getfield Turing/TAPE [I
	arraylength
	if_icmpge printTape(0)6
	aload_0
	getfield Turing/TAPE [I
	iload 1
	iaload
	invokestatic io/print(error)V
	iload 1
	iconst_1
	iadd
	istore 1

	goto printTape(0)5

printTape(0)6:
	invokestatic io/println()V
	invokestatic io/println()V
	iconst_1
	ireturn
.end method

.method public trans()Z
	.limit stack 999
	.limit locals 6

	aload_0
	getfield Turing/TAPE [I
	aload_0
	getfield Turing/curPos I
	iaload
	istore 4

	aload_0
	getfield Turing/WTABLE [I
	aload_0
	iload 4
	aload_0
	getfield Turing/curState I
	invokevirtual Turing/ss2i(II)I
	iaload
	istore 1

	aload_0
	getfield Turing/MTABLE [I
	aload_0
	iload 4
	aload_0
	getfield Turing/curState I
	invokevirtual Turing/ss2i(II)I
	iaload
	istore 2

	aload_0
	getfield Turing/NTABLE [I
	aload_0
	iload 4
	aload_0
	getfield Turing/curState I
	invokevirtual Turing/ss2i(II)I
	iaload
	istore 3

	aload_0
	getfield Turing/TAPE [I
	aload_0
	getfield Turing/curPos I
	iload 1
	iastore

	aload_0
	aload_0
	getfield Turing/curPos I
	iload 2
	iadd
	putfield Turing/curPos I

	aload_0
	iload 3
	putfield Turing/curState I

	aload_0
	getfield Turing/H I
	aload_0
	getfield Turing/curState I
	if_icmplt trans(0)2
	ifeq trans(0)2
	aload_0
	getfield Turing/curState I
	aload_0
	getfield Turing/H I
	if_icmplt trans(0)2
	ifeq trans(0)2
	ifeq trans(0)2
	iconst_0
	istore 5

	goto trans(0)1

trans(0)2:
	iconst_1
	istore 5


trans(0)1:
	iload 5
	ireturn
.end method

.method public initGeneric(III)[I
	.limit stack 999
	.limit locals 6

	aload_0
	iload_2
	putfield Turing/NUM_SYMBOLS I

	aload_0
	iload_1
	putfield Turing/NUM_STATES I

	aload_0
	getfield Turing/NUM_SYMBOLS I
	aload_0
	getfield Turing/NUM_STATES I
	imul
	istore 5

	aload_0
	iconst_0
	iconst_1
	isub
	putfield Turing/H I

	aload_0
	iconst_0
	iconst_1
	isub
	putfield Turing/L I

	aload_0
	iconst_1
	putfield Turing/R I

	aload_0
	iload 5
	newarray int
	putfield Turing/WTABLE [I

	aload_0
	iload 5
	newarray int
	putfield Turing/MTABLE [I

	aload_0
	iload 5
	newarray int
	putfield Turing/NTABLE [I

	iload_3
	newarray int
	astore 4

	aload_0
	iconst_0
	putfield Turing/curState I

	aload_0
	aload 4
	arraylength
	iconst_2
	idiv
	putfield Turing/curPos I

	aload 4
	areturn
.end method

.method public ss2i(II)I
	.limit stack 999
	.limit locals 3

	iload_1
	aload_0
	getfield Turing/NUM_STATES I
	imul
	iload_2
	iadd
	ireturn
.end method

.method public setTrans(IIIII)Z
	.limit stack 999
	.limit locals 6

	aload_0
	getfield Turing/WTABLE [I
	aload_0
	iload_1
	iload_2
	invokevirtual Turing/ss2i(II)I
	iload_3
	iastore

	aload_0
	getfield Turing/MTABLE [I
	aload_0
	iload_1
	iload_2
	invokevirtual Turing/ss2i(II)I
	iload_4
	iastore

	aload_0
	getfield Turing/NTABLE [I
	aload_0
	iload_1
	iload_2
	invokevirtual Turing/ss2i(II)I
	iload_5
	iastore

	iconst_1
	ireturn
.end method

