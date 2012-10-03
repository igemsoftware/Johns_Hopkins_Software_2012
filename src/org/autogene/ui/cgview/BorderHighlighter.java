/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.ui.cgview;

import java.awt.*;
import javax.swing.text.*;

/**
 *
 * @author Robert
 */
public class BorderHighlighter extends DefaultHighlighter {
  public BorderHighlighter(Color c) {
    painter = (c == null ? sharedPainter : new BorderHighlightPainter(c));
  }


  public Object addHighlight(int p0, int p1) throws BadLocationException {
    return addHighlight(p0, p1, painter);
  }

  public void setDrawsLayeredHighlights(boolean newValue) {
    if (newValue == false) {
      throw new IllegalArgumentException(
          "UnderlineHighlighter only draws layered highlights");
    }
    super.setDrawsLayeredHighlights(true);
  }

  // Painter for underlined highlights
  public static class BorderHighlightPainter extends
      LayeredHighlighter.LayerPainter {
    public BorderHighlightPainter(Color c) {
      color = c;
    }

    public void paint(Graphics g, int offs0, int offs1, Shape bounds,
        JTextComponent c) {
      // Do nothing: this method will never be called
    }

    public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds,
        JTextComponent c, View view) {
      g.setColor(color == null ? c.getSelectionColor() : color);

      Rectangle alloc = null;
      if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
        if (bounds instanceof Rectangle) {
          alloc = (Rectangle) bounds;
        } else {
          alloc = bounds.getBounds();
        }
      } else {
        try {
          Shape shape = view.modelToView(offs0,
              Position.Bias.Forward, offs1,
              Position.Bias.Backward, bounds);
          alloc = (shape instanceof Rectangle) ? (Rectangle) shape : shape.getBounds();
        } catch (BadLocationException e) {
          return null;
        }
      }

      FontMetrics fm = c.getFontMetrics(c.getFont());
      int baseline = alloc.y + alloc.height - fm.getDescent() + 1;
      ((Graphics2D)(g)).setStroke(new BasicStroke(2));
      g.drawLine(alloc.x, baseline, alloc.x + alloc.width, baseline);
      g.drawLine(alloc.x, alloc.y, alloc.x + alloc.width, alloc.y);
      g.drawLine(alloc.x, alloc.y, alloc.x, baseline);
      g.drawLine(alloc.x + alloc.width, alloc.y, alloc.x + alloc.width, baseline);

      return alloc;
    }

    protected Color color; // The color for the underline
  }

  // Shared painter used for default highlighting
  protected static final Highlighter.HighlightPainter sharedPainter = new BorderHighlightPainter(
      null);

  // Painter used for this highlighter
  protected Highlighter.HighlightPainter painter;
}
