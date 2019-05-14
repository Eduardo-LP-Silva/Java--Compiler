.class public MonteCarloPi
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public performSingleEstimate()Z
	.limit stack 999
	.limit locals 5

	iconst_0
	bipush 100
	isub
	bipush 100
	invokestatic MathUtils/random(I I)V
	istore 0

	iconst_0
	bipush 100
	isub
	bipush 100
	invokestatic MathUtils/random(I I)V
	istore 1

	iload 0
	iload 0
	imul
	iload 1
	iload 1
	imul
	iadd
	bipush 100
	idiv
	istore 3

	iload 3
	bipush 100
	if_icmpge performSingleEstimate8890
	iconst_1
	istore 2

	goto performSingleEstimate913

performSingleEstimate8890:
	iconst_0
	istore 2


performSingleEstimate913:
	iload 2
	ireturn
.end method

.method public estimatePi100(I)I
	.limit stack 999
	.limit locals 5

	iconst_0
	istore 2

	iconst_0
	istore 1


estimatePi100517:
	iload 2
	iload_0
	if_icmpge estimatePi1003067
	aload_0
	invokevirtual MonteCarloPi/performSingleEstimate()Z
	iload 1
	iconst_1
	iadd
	istore 1

	goto estimatePi1001017

estimatePi1002564:

estimatePi1001017:
	iload 2
	iconst_1
	iadd
	istore 2

	goto estimatePi100517

estimatePi1003067:
	sipush 400
	iload 1
	imul
	iload_0
	idiv
	istore 3

	iload 3
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 999
	.limit locals 3

	invokestatic ioPlus/requestNumber()V
	istore 2

	new MonteCarloPi
	dup
	invokenonvirtual MonteCarloPi/<init>()V
	iload 2
	invokevirtual MonteCarloPi/estimatePi100(I)I
	istore 1

	iload 1
	invokestatic ioPlus/printResult(I)V
	return
.end method

