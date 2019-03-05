package tree
class SBBSTNode(n: Pair<Int, Int>) {
    var left: SBBSTNode? = null
    var right: SBBSTNode? = null
    var data: Pair<Int, Int> = n
    var height: Int = 0

    init {
        left = null
        right = null
        height = 0
    }
}

class SelfBalancingBinarySearchTree {
    private var root: SBBSTNode? = null

    init {
        root = null
    }

    fun clear() {
        root = null
    }

    fun insert(data: Pair<Int, Int>) {
        root = insert(data, root)
    }

    private fun height(t: SBBSTNode?): Int {
        return t?.height ?: -1
    }

    private fun max(lhs: Int, rhs: Int): Int {
        return if (lhs > rhs) lhs else rhs
    }

    private fun insert(x: Pair<Int,Int>, node: SBBSTNode?): SBBSTNode {
        var t = node
        if (t == null)
            t = SBBSTNode(x)
        else if (x.first < t.data.first) {
            t.left = insert(x, t.left)
            if (height(t.left) - height(t.right) == 2)
                t = if (x.first < t.left!!.data.first)
                    rotateWithLeftChild(t)
                else
                    doubleWithLeftChild(t)
        } else if (x.first > t.data.first) {
            t.right = insert(x, t.right)
            if (height(t.right) - height(t.left) == 2)
                t = if (x.first > t.right!!.data.first)
                    rotateWithRightChild(t)
                else
                    doubleWithRightChild(t)
        }  // Duplicate; do nothing
        t.height = max(height(t.left), height(t.right)) + 1
        return t
    }

    private fun rotateWithLeftChild(k2: SBBSTNode): SBBSTNode {
        val k1 = k2.left
        k2.left = k1!!.right
        k1.right = k2
        k2.height = max(height(k2.left), height(k2.right)) + 1
        k1.height = max(height(k1.left), k2.height) + 1
        return k1
    }

    private fun rotateWithRightChild(k1: SBBSTNode): SBBSTNode {
        val k2 = k1.right
        k1.right = k2!!.left
        k2.left = k1
        k1.height = max(height(k1.left), height(k1.right)) + 1
        k2.height = max(height(k2.right), k1.height) + 1
        return k2
    }

    private fun doubleWithLeftChild(k3: SBBSTNode): SBBSTNode {
        k3.left = rotateWithRightChild(k3.left!!)
        return rotateWithLeftChild(k3)
    }

    private fun doubleWithRightChild(k1: SBBSTNode): SBBSTNode {
        k1.right = rotateWithLeftChild(k1.right!!)
        return rotateWithRightChild(k1)
    }

    fun countNodes(): Int {
        return countNodes(root)
    }

    private fun countNodes(r: SBBSTNode?): Int {
        return if (r == null)
            0
        else {
            var l = 1
            l += countNodes(r.left)
            l += countNodes(r.right)
            l
        }
    }

    fun search(`val`: Pair<Int, Int>): Boolean {
        return search(root, `val`)
    }

    private fun search(node: SBBSTNode?, `val`: Pair<Int,Int>): Boolean {
        var r = node
        var found = false
        while (r != null && !found) {
            val rval = r.data
            if (`val`.first < rval.first)
                r = r.left
            else if (`val`.first > rval.first)
                r = r.right
            else {
                found = true
                break
            }
            found = search(r, `val`)
        }
        return found
    }

    fun inOrder() {
        inOrder(root)
    }

    private fun inOrder(r: SBBSTNode?) {
        if (r != null) {
            inOrder(r.left)
            print(r.data.toString() + " ")
            inOrder(r.right)
        }
    }

    fun preOrder() {
        preOrder(root)
    }

    private fun preOrder(r: SBBSTNode?) {
        if (r != null) {
            print(r.data.toString() + " ")
            preOrder(r.left)
            preOrder(r.right)
        }
    }

    fun postOrder() {
        postOrder(root)
    }

    private fun postOrder(r: SBBSTNode?) {
        if (r != null) {
            postOrder(r.left)
            postOrder(r.right)
            print(r.data.toString() + " ")
        }
    }
}