import io.github.vigoo.repomorph.contexts.TestMorphContext
import io.github.vigoo.repomorph.api.ScriptAPI._

implicit val context = TestMorphContext


foreach { file called "AssemblyInfo.cs" } {
  removeLines(line => line.startsWith("[assembly: AssemblyVersion(") ||
                      line.startsWith("[assembly: AssemblyFileVersion(")) }
move { "SmartCore\\General" } to "SmartCore\\SmartCore.General\\cs"


delete { directory called "SmartCore\\SmartCore.General\\cs\\PointStorage"}
move { "SmartCore\\Core" } to "SmartCore\\SmartCore.Core\\cs"



move { "SmartCore\\Annotations" } to "SmartCore\\SmartCore.Controls.Annotations\\cs"



delete { files in "SmartCore\\SmartCore.Controls.Annotations\\cs\\Properties" except "AssemblyInfo.cs" }
delete { file called "StandaloneApp.xaml.cs"}

move { "SmartCore\\ModelPublic"} to "SmartCore\\SmartCore.Model.Public\\cs"


delete { file("SmartCore\\SmartCore.Model.Public\\cs\\Features\\IWidth.cs") }
delete { file("SmartCore\\SmartCore.Model.Public\\cs\\Features\\IWidth3D.cs") }



