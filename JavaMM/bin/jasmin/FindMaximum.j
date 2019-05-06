.class public FindMaximum
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public find_maximum([I)I
	.limit stack 5
	.limit locals 6

	bipush 10
	istore 5

	iconst_1
	istore 2

	iconst_0
	istore 3

	iconst_1
	istore 4


find_maximum3716:
	iload 5
	bipush 10
	if_icmpge find_maximum7800
	iconst_0
	istore 3

goto find_maximum3716

find_maximum7800:
	iconst_1
	ireturn
.end method

.method public build_test_arr()I
	.limit stack 0
	.limit locals 1

	iconst_5
	newarray int
	aload_0
	putfield FindMaximum/test_arr [I

	bipush 14
	aload_0
	putfield FindMaximum/test_arr [I
	iconst_0
	iaload

	bipush 28
	aload_0
	putfield FindMaximum/test_arr [I
	iconst_1
	iaload

	iconst_0
	aload_0
	putfield FindMaximum/test_arr [I
	iconst_2
	iaload

	iconst_0
	iconst_5
	isub
	aload_0
	putfield FindMaximum/test_arr [I
	iconst_3
	iaload

	bipush 12
	aload_0
	putfield FindMaximum/test_arr [I
	iconst_4
	iaload

	iconst_0
	ireturn
.end method

.method public get_array()[I
	.limit stack 0
	.limit locals 1

	aload_0
	getfield FindMaximum/test_arr [I
	areturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 2

	new FindMaximum
	dup
	invokenonvirtual FindMaximum/<init>()V
	astore 1

	aload 1
	invokevirtual FindMaximum/build_test_arr()I
	aload 1
	aload 1
	invokevirtual FindMaximum/get_array()[I
	invokevirtual FindMaximum/find_maximum([I)I
	invokestatic ioPlus/printResult(I)V
	return
.end method

