package io.github.vigoo.repomorph

import io.github.vigoo.repomorph.contexts.MorphContext

trait Script {
  def apply(implicit context: MorphContext): Unit
}
