
platform :android do
  lane :clean do
    gradle(task: "clean")
  end

  lane :test do
    gradle(task: "clean assembleAndroidTest")

    $projects.each do |project|
        android_test(":#{project}:testDebug", "#{project}")
    end
  end

  lane :test_lowmemory do
    gradle(task: "clean assembleAndroidTest")

    $projects.each do |project|
        sh "killall -v java"
        android_test(":#{project}:testDebug", "#{project}")
    end
  end

  lane :assemble do
    gradle(task: "clean")

    $projects.each do |project|
        gradle(task: ":#{project}:assembleRelease")
        copy_artifacts(
          target_path: "artifacts/#{project}",
          artifacts: ["#{project}/build/outputs"],
        )
    end
  end

  lane :assemble_lowmemory do
    gradle(task: "clean")

    $projects.each do |project|
        sh "killall -v java"
        gradle(task: ":#{project}:assembleRelease")
        copy_artifacts(
          target_path: "artifacts/#{project}",
          artifacts: ["#{project}/build/outputs"],
        )
    end
  end
end


# single test with archive.
def android_test(task, path)
    begin
        gradle(task: "#{task}")
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
    gradle(task: "clean #{task}")
    copy_artifacts(
      target_path: "artifacts/#{path}",
      artifacts: ["#{path}/build/outputs"],
    )
end
