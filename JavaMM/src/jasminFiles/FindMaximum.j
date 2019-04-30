.class public FindMaximum
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public find_maximum(I)I
	.limit stack 7
	.limit locals 8

	iconst_0
	istore 4

	iconst_1
	istore 5

	iload 4
	ifeq find_maximum9089
	iload 5
	ifeq find_maximum9089
	iconst_1
	goto find_maximum343

find_maximum9089:
	iconst_0

find_maximum343:
	istore 6

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
	.limit stack 2
	.limit locals 2

	new FindMaximum
	dup
	invokenonvirtual FindMaximum/<init>()V
	astore 1

	aload 1
	iconst_1
	invokevirtual FindMaximum/find_maximum(I)I
	return
.end method

