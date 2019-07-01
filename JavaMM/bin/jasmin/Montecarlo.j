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
	invokestatic MathUtils/random(II)I
	istore 1

	iconst_0
	bipush 100
	isub
	bipush 100
	invokestatic MathUtils/random(II)I
	istore 2

	iload 1
	iload 1
	imul
	iload 2
	iload 2
	imul
	iadd
	bipush 100
	idiv
	istore 4

	iload 4
	bipush 100
	if_icmpge performSingleEstimate(0)2
	iconst_1
	istore 3

	goto performSingleEstimate(0)1

performSingleEstimate(0)2:
	iconst_0
	istore 3


performSingleEstimate(0)1:
	iload 3
	ireturn
.end method

.method public estimatePi100(I)I
	.limit stack 999
	.limit locals 5

	iconst_0
	istore 3

	iconst_0
	istore 2


estimatePi100(1)1:
	iload 3
	iload_1
	if_icmpge estimatePi100(1)2
	aload_0
	invokevirtual MonteCarloPi/performSingleEstimate()Z
	ifeq estimatePi100(1)4
	iload 2
	iconst_1
	iadd
	istore 2

	goto estimatePi100(1)3

estimatePi100(1)4:

estimatePi100(1)3:
	iload 3
	iconst_1
	iadd
	istore 3

	goto estimatePi100(1)1

estimatePi100(1)2:
	sipush 400
	iload 2
	imul
	iload_1
	idiv
	istore 4

	iload 4
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 999
	.limit locals 3

	invokestatic ioPlus/requestNumber()I
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

