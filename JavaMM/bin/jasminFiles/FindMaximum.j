.class public FindMaximum
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public find_maximum([I)I
	.limit locals 4

	iconst_1
	istore_1

	aload_0
	istore_2

	iload_2
	ireturn
.end method

.method public build_test_arr()I
	.limit locals 0

	putfield FindMaximum [I

	bipush 14
	putfield FindMaximum [I

	bipush 28
	putfield FindMaximum [I

	iconst_0
	putfield FindMaximum [I

	iconst_0
	iconst_5
	isub
	putfield FindMaximum [I

	bipush 12
	putfield FindMaximum [I

	iconst_0
	ireturn
.end method

.method public get_array()[I
	.limit locals 0

	getfield FindMaximum [I
	areturn
.end method

.method public static main([Ljava/lang/String;)V
	.limit locals 2

	new FindMaximum
	invokenonvirtual FindMaximum()V
	astore_1

	aload_1
	invokevirtual FindMaximum/build_test_arr()I
	aload_1
	aload_1
	invokevirtual FindMaximum/get_array()[I
	invokevirtual FindMaximum/find_maximum([I )I
	invokevirtual ioPlus/printResult(I )V
	return
.end method

