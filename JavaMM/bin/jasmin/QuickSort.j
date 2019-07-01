.class public Quicksort
.super java/lang/Object


.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 999
	.limit locals 4

	bipush 10
	newarray int
	astore 1

	iconst_0
	istore 2


main(1)1:
	iload 2
	aload 1
	arraylength
	if_icmpge main(1)2
	aload 1
	iload 2
	aload 1
	arraylength
	iload 2
	isub
	iastore

	iload 2
	iconst_1
	iadd
	istore 2

	goto main(1)1

main(1)2:
	new Quicksort
	dup
	invokenonvirtual Quicksort/<init>()V
	astore 3

	aload 3
	aload 1
	invokevirtual Quicksort/quicksort([I)Z
	pop
	aload 3
	aload 1
	invokevirtual Quicksort/printL([I)Z
	pop
	return
.end method

.method public printL([I)Z
	.limit stack 999
	.limit locals 3

	iconst_0
	istore 2


printL(1)1:
	iload 2
	aload_1
	arraylength
	if_icmpge printL(1)2
	aload_1
	iload 2
	iaload
	invokestatic io/println(I)V
	iload 2
	iconst_1
	iadd
	istore 2

	goto printL(1)1

printL(1)2:
	iconst_1
	ireturn
.end method

.method public quicksort([I)Z
	.limit stack 999
	.limit locals 2

	aload_0
	aload_1
	iconst_0
	aload_1
	arraylength
	iconst_1
	isub
	invokevirtual Quicksort/quicksort([III)Z
	ireturn
.end method

.method public quicksort([III)Z
	.limit stack 999
	.limit locals 5

	iload_2
	iload_3
	if_icmpge quicksort(3)2
	aload_0
	aload_1
	iload_2
	iload_3
	invokevirtual Quicksort/partition([III)I
	istore 4

	aload_0
	aload_1
	iload_2
	iload 4
	iconst_1
	isub
	invokevirtual Quicksort/quicksort([III)Z
	pop
	aload_0
	aload_1
	iload 4
	iconst_1
	iadd
	iload_3
	invokevirtual Quicksort/quicksort([III)Z
	pop
	goto quicksort(3)1

quicksort(3)2:

quicksort(3)1:
	iconst_1
	ireturn
.end method

.method public partition([III)I
	.limit stack 999
	.limit locals 8

	aload_1
	iload_3
	iaload
	istore 4

	iload_2
	istore 5

	iload_2
	istore 6


partition(3)1:
	iload 6
	iload_3
	if_icmpge partition(3)2
	aload_1
	iload 6
	iaload
	iload 4
	if_icmpge partition(3)4
	aload_1
	iload 5
	iaload
	istore 7

	aload_1
	iload 5
	aload_1
	iload 6
	iaload
	iastore

	aload_1
	iload 6
	iload 7
	iastore

	iload 5
	iconst_1
	iadd
	istore 5

	goto partition(3)3

partition(3)4:

partition(3)3:
	iload 6
	iconst_1
	iadd
	istore 6

	goto partition(3)1

partition(3)2:
	aload_1
	iload 5
	iaload
	istore 7

	aload_1
	iload 5
	aload_1
	iload_3
	iaload
	iastore

	aload_1
	iload_3
	iload 7
	iastore

	iload 5
	ireturn
.end method

