class Solution {
    public int minPathSum(int[][] grid) {
        int sum = 0;       
        int m = grid[0].length;
        int n = grid.length;             
        if (m == 1) {            
            for (int i = 0; i < n; i++) {
                sum += grid[i][0];
            }
            return sum;
        }
        if (n == 1) {
            for (int i = 0; i < m; i++) {
                sum += grid[0][i];
            }
            return sum;
        }
  
        int[][] ng = new int[n][m];
        ng[0][0] = grid[0][0];

        for (int i = 1; i < n; i++) {
            ng[i][0] += grid[i][0] + ng[i-1][0];
        }
        for (int i = 1; i < m; i++) {
            ng[0][i] += grid[0][i] + ng[0][i-1];
        }
        for(int i = 1; i < n; i++) {
            for(int j = 1; j < m; j++) {
                ng[i][j] = grid[i][j] + Math.min(ng[i][j-1], ng[i-1][j]);
            }
        }
        return ng[n-1][m-1];       
    }
}