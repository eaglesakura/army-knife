
platform :android do
  lane :clean do
    gradle(task: "clean")
  end

  lane :test do
    $projects.each do |project|
        android_test(":#{project}:testDebug", "#{project}")
        android_test(":#{project}:connectedAndroidTest", "#{project}")
    end
  end

  lane :assemble do
    $projects.each do |project|
        gradle(task: ":#{project}:assembleRelease")
        copy_artifacts(
          target_path: "artifacts/#{project}",
          artifacts: ["#{project}/build/outputs"],
        )
    end
  end

  lane :install_snapshot do
    gradle(task: "--parallel -Pinstall_snapshot assembleRelease uploadArchives")
  end

end


# single test with archive.
def android_test(task, path)
    begin
        gradle(task: "--no-daemon #{task}")
        copy_artifacts(
          target_path: "artifacts/#{path}",
          artifacts: ["#{path}/build/reports"],
        )
    rescue => e
        copy_artifacts(
          target_path: "artifacts/#{path}",
          artifacts: ["#{path}/build/reports"],
        )
        Kernel.abort
    end
end

# single assemble with archive.
def android_assemble(task, path)
    gradle(task: "--no-daemon #{task}")
    copy_artifacts(
      target_path: "artifacts/#{path}",
      artifacts: ["#{path}/build/outputs"],
    )
end
