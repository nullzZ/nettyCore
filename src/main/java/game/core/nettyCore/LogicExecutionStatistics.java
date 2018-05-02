/**
 * 
 */
package game.core.nettyCore;

/**
 * 逻辑执行(时间)统计
 * 
 * @author nullzZ
 *
 */
public interface LogicExecutionStatistics {
	boolean shouldExecuteInIOThread(String method);

	void saveExecutionMillTime(String method, int exeTime);
}
