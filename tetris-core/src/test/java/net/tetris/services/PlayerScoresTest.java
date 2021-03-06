package net.tetris.services;

import static net.tetris.services.PlayerScores.*;
import net.tetris.dom.GameLevel;
import net.tetris.dom.TetrisFigure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class PlayerScoresTest {

    private PlayerScores playerScores;

    @Mock
    private GameLevel level;

    @Before
    public void setUp() throws Exception {
        playerScores = new PlayerScores(0);
        playerScores.levelChanged(0, level);
    }

    @Test
    public void shouldCalcScoreWhen1LineRemoved(){
        setFiguresToOpenCount(1);
        playerScores.linesRemoved(1);

        assertEquals(ONE_LINE_REMOVED_SCORE, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen1LineRemovedWithLevel(){
        setFiguresToOpenCount(2);
        playerScores.linesRemoved(1);

        assertEquals(2 * ONE_LINE_REMOVED_SCORE, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen2LineRemoved(){
        setFiguresToOpenCount(2);
        playerScores.linesRemoved(2);

        assertEquals(2 * TWO_LINES_REMOVED_SCORE, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen3LineRemoved(){
        setFiguresToOpenCount(2);
        playerScores.linesRemoved(3);

        assertEquals(2 * THREE_LINES_REMOVED_SCORE, playerScores.getScore());
    }

    @Test
    public void shouldCalcScoreWhen4LineRemoved() {
        setFiguresToOpenCount(5);
        playerScores.linesRemoved(4);

        assertEquals(5 * FOUR_LINES_REMOVED_SCORE, playerScores.getScore());
    }

    private OngoingStubbing<Integer> setFiguresToOpenCount(int FiguresToOpenCount) {
        return when(level.getFigureTypesToOpenCount()).thenReturn(FiguresToOpenCount);
    }

    @Test
    public void shouldAccumulateScoreWhenLineRemoved(){
        setFiguresToOpenCount(1);

        playerScores.linesRemoved(1);
        playerScores.linesRemoved(3);

        assertEquals(ONE_LINE_REMOVED_SCORE + THREE_LINES_REMOVED_SCORE,
                playerScores.getScore());
    }

    @Test
    public void shouldCalculateGlassOverFlow(){
        setFiguresToOpenCount(1);

        playerScores.glassOverflown();

        assertEquals(GLASS_OVERFLOWN_PENALTY, playerScores.getScore());
    }

    @Test
    public void shouldCalculateGlassOverFlowWhenOtherLevel(){
        setFiguresToOpenCount(5);

        playerScores.glassOverflown();

        assertEquals(5 * GLASS_OVERFLOWN_PENALTY, playerScores.getScore());
    }

    @Test
    public void shouldAccumulateGlassOverFlow(){
        setFiguresToOpenCount(1);

        playerScores.linesRemoved(1);

        playerScores.glassOverflown();

        assertEquals(ONE_LINE_REMOVED_SCORE + GLASS_OVERFLOWN_PENALTY,
                playerScores.getScore());
    }

    @Test
    public void shouldCalculateWhenFigureDropped(){
        setFiguresToOpenCount(1);

        playerScores.linesRemoved(1);

        playerScores.figureDropped(new TetrisFigure(0,0, "#"));

        assertEquals(ONE_LINE_REMOVED_SCORE + FIGURE_DROPPED_SCORE,
                playerScores.getScore());
    }

    @Test
    public void shouldCalculateWithBaseScore(){
        setFiguresToOpenCount(1);

        playerScores = new PlayerScores(-5000);
        playerScores.levelChanged(0, level);

        assertEquals(-5000, playerScores.getScore());

        playerScores.linesRemoved(1);

        assertEquals(-5000 + ONE_LINE_REMOVED_SCORE, playerScores.getScore());
    }



}
