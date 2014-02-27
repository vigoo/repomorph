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

move { "SmartCore\\BaseControls" } to "SmartCore\\SmartCore.Controls.Base\\cs"
delete { files in "SmartCore\\SmartCore.Controls.Base\\cs\\Properties" except "AssemblyInfo.cs" }
move { "SmartCore\\SmartCore.Controls.Base\\cs\\GUIBuilder\\Schema"} to "SmartCore\\SmartCore.Controls.Base\\doc"
move { "SmartCore\\SmartCore.Controls.Base\\cs\\Localization\\SPECS.txt"} to "SmartCore\\SmartCore.Controls.Base\\doc"
delete { file("SmartCore\\SmartCore.Controls.Base\\cs\\PropertyEditor\\Controls\\SizeTolerancePropertyControl.resx")}
delete { files in "SmartCore\\SmartCore.Controls.Base\\cs\\Resources" withExtension ".ttf" }
move { "SmartCore\\SmartCore.Controls.Base\\cs\\Resources"} to "SmartCore\\SmartCore.Controls.Base\\resources"
delete { file ("SmartCore\\SmartCore.Controls.Base\\cs\\FontProvider.cs")}

move { "SmartCore\\BaseView" } to "SmartCore\\SmartCore.View.Base\\cs"

move { "SmartCore\\UIPublic" } to "SmartCore\\SmartCore.UI.Public\\cs"
delete { directory called "SmartCore\\SmartCore.UI.Public\\cs\\Diagrams" }

move { "SmartCore\\Actions" } to "SmartCore\\SmartCore.Actions\\cs"

move { "SmartCore\\SppVersioning" } to "SmartCore\\SmartCore.SppVersioning\\fs"
foreach { file called "Versioning.fs" } {
  removeLines(line => line.startsWith("[<assembly: System.Reflection.AssemblyVersion(") ||
                      line.startsWith("[<assembly: System.Reflection.AssemblyFileVersion(")) }

move { "SmartCore\\GDTApi" } to "SmartCore\\GDTApi\\cpp"

foreach { file("SmartCore\\GDTApi\\cpp\\win33lib\\common\\stdtchar.cpp")} {
  removeLines(line => line.startsWith("#include \"stdafx.h\"")) }
foreach { file("SmartCore\\GDTApi\\cpp\\app.rc")} {
  removeLines{line => line contains "ICON" }
}

move { "SmartCore\\Model" } to "SmartCore\\SmartCore.Model\\cs"
delete { directory called "SmartCore\\SmartCore.Model\\cs\\Diagrams" }
move { "SmartCore\\SmartCore.Model\\cs\\Features\\ConstructionScripts\\Scripts" } to "SmartCore\\SmartCore.Model\\resource\\Features\\ConstructionScripts\\Scripts"
move { "SmartCore\\SmartCore.Model\\cs\\Schema" } to "SmartCore\\SmartCore.Model\\doc\\Schema"
delete { files("Circle.cs", "Cone.cs", "Cylinder.cs", "Line.cs", "Pattern.cs", "Plane.cs", "Sphere.cs", "Width.cs", "Width2D.cs", "Width3D.cs") from "SmartCore\\SmartCore.Model\\cs\\Features"}
delete { directory called "SmartCore\\SmartCore.Model\\cs\\PermaLink" }

move { "SmartCore\\CoreLegacy" } to "SmartCore\\SmartCore.Core.Legacy\\cs"

move { "SmartCore\\View" } to "SmartCore\\SmartCore.View\\cs"
delete { file("SmartCore\\SmartCore.View\\cs\\InputProcessors\\Helper\\TemporaryNonProjectFeaturePicker.cs") }

move { "SmartCore\\CoreControls" } to "SmartCore\\SmartCore.Controls.Core\\cs"
delete { files in "SmartCore\\SmartCore.Controls.Core\\cs" withExtension ".resx" }

move { "SmartCore\\Controls" } to "SmartCore\\SmartCore.Controls\\cs"
delete { files in "SmartCore\\SmartCore.Controls\\cs" withExtension ".resx" }
delete { files in "SmartCore\\SmartCore.Controls\\cs" withExtension ".licx" }

move { "SmartCore\\UI" } to "SmartCore\\SmartCore.UI\\cs"
delete { files in "SmartCore\\SmartCore.UI\\cs" withExtension ".resx" }
move { "SmartCore\\SmartCore.UI\\cs\\Resources" } to "SmartCore\\SmartCore.UI\\resource"
move.files.in("SmartCore\\SmartCore.UI\\cs\\").withExtension("xml").to("SmartCore\\SmartCore.UI\\content")
delete { file("SmartCore\\SmartCore.UI\\cs\\GUI\\Forms\\MainWindow.cs")}




