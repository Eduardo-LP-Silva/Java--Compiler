.class public FindMaximum
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public find_maximum(I)I
	.limit stack 4
	.limit locals 5

	iconst_2
	istore 1

	bipush 10
	istore 2

	iload 1
	bipush 10
	imul
	istore 3

	iload 3
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 1
	.limit locals 1

	aload_0
	iconst_1
	invokevirtual FindMaximum/find_maximum(I)I
	return
.end method

